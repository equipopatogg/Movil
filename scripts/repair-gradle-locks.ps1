param([switch]$UseWindowsTrust, [switch]$RepairOnly)
$ErrorActionPreference = 'Stop'
$project = [IO.Path]::GetFullPath((Split-Path -Parent $PSScriptRoot))
$wrapper = Join-Path $project 'gradlew.bat'
if (-not (Test-Path -LiteralPath $wrapper)) { throw 'No se encontro el Wrapper del proyecto.' }
if (-not $env:JAVA_HOME) {
    $studioJdk = 'C:\Program Files\Android\Android Studio\jbr'
    if (Test-Path -LiteralPath (Join-Path $studioJdk 'bin\java.exe')) { $env:JAVA_HOME = $studioJdk }
}

# Only the transform-cache lock metadata of this Gradle version is considered.
# No dependency lockfiles, downloaded libraries, credentials or sources are removed.
$gradleHome = if ($env:GRADLE_USER_HOME) { $env:GRADLE_USER_HOME } else {
    Join-Path $env:USERPROFILE '.gradle'
}
$cacheRoot = [IO.Path]::GetFullPath((Join-Path $gradleHome 'caches\9.5.0\transforms'))
$locksRoot = [IO.Path]::GetFullPath((Join-Path $cacheRoot '.internal\locks'))
$requiredPrefix = $cacheRoot.TrimEnd('\') + '\'
if (-not $locksRoot.StartsWith($requiredPrefix, [StringComparison]::OrdinalIgnoreCase)) {
    throw 'La ruta de bloqueos no corresponde a la cache prevista.'
}

Push-Location -LiteralPath $project
try {
    Write-Host 'Deteniendo los procesos Gradle del usuario...'
    & $wrapper --stop
    if ($LASTEXITCODE -ne 0) { throw 'No se pudo detener Gradle. Cierra Android Studio y vuelve a ejecutar.' }
    $repaired = 0
    $backup = Join-Path $env:TEMP ('PactoGG-gradle-locks-' + (Get-Date -Format 'yyyyMMdd-HHmmss-fff'))
    if (Test-Path -LiteralPath $locksRoot) {
        $folder = Get-Item -LiteralPath $locksRoot -Force
        if ($folder.LinkType -in @('SymbolicLink', 'Junction')) {
            throw 'La carpeta de bloqueos es un enlace; no se modifica.'
        }
        foreach ($file in Get-ChildItem -LiteralPath $locksRoot -File -Filter '*.lock') {
            $target = [IO.Path]::GetFullPath($file.FullName)
            if ($file.Name -notmatch '^[a-fA-F0-9]{32}\.lock$' -or
                -not $target.StartsWith($locksRoot.TrimEnd('\') + '\',
                    [StringComparison]::OrdinalIgnoreCase)) {
                continue
            }
            # Exclusive access prevents changing a lock that a running process currently owns.
            $stream = [IO.File]::Open($target, [IO.FileMode]::Open, [IO.FileAccess]::Read, [IO.FileShare]::None)
            try {
                $emptyState = $stream.Length -eq 17
                while ($emptyState -and $stream.Position -lt $stream.Length) {
                    if ($stream.ReadByte() -ne 0) { $emptyState = $false }
                }
            } finally { $stream.Dispose() }
            if ($emptyState) {
                New-Item -ItemType Directory -Path $backup -Force | Out-Null
                Copy-Item -LiteralPath $target -Destination (Join-Path $backup $file.Name) -ErrorAction Stop
                Remove-Item -LiteralPath $target -Force -ErrorAction Stop
                $repaired++
            }
        }
    }
    Write-Host "Archivos de bloqueo vacios retirados: $repaired"
    if ($repaired -gt 0) { Write-Host "Respaldo: $backup" }
    if (-not $RepairOnly) {
        & (Join-Path $PSScriptRoot 'check-android.ps1') -UseWindowsTrust:$UseWindowsTrust
    }
} finally { Pop-Location }

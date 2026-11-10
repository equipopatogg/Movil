param([string]$GitPath, [switch]$CheckGitOnly, [switch]$CheckDeliveryOnly)
$ErrorActionPreference = 'Stop'

function Find-DeliveryGit {
    if ($GitPath) {
        if (-not (Test-Path -LiteralPath $GitPath -PathType Leaf)) { throw 'La ruta indicada de Git no existe.' }
        return (Resolve-Path -LiteralPath $GitPath).Path
    }
    $command = Get-Command git -CommandType Application -ErrorAction SilentlyContinue | Select-Object -First 1
    if ($command) { return $command.Source }
    $candidates = @(
        'C:\Program Files\Git\cmd\git.exe',
        'C:\Program Files\Git\bin\git.exe',
        'C:\Program Files (x86)\Git\cmd\git.exe',
        (Join-Path $env:USERPROFILE 'AppData\Local\Programs\Git\cmd\git.exe')
    )
    foreach ($registryPath in @('HKCU:\SOFTWARE\GitForWindows', 'HKLM:\SOFTWARE\GitForWindows')) {
        $installation = Get-ItemProperty -LiteralPath $registryPath -ErrorAction SilentlyContinue
        if ($installation.InstallPath) { $candidates += Join-Path $installation.InstallPath 'cmd\git.exe' }
    }
    if (Get-Command Get-AppxPackage -ErrorAction SilentlyContinue) {
        try {
            foreach ($package in @(Get-AppxPackage -Name '*CodexPrimaryRuntime*' -ErrorAction Stop)) {
                $candidates += Join-Path $package.InstallLocation 'dependencies\native\git\cmd\git.exe'
            }
        } catch { Write-Verbose 'No se pudo consultar la ubicacion del Git incluido en la aplicacion.' }
    }
    # Optional portable Git already present on the reviewed machine. No installation or PATH changes.
    $candidates += 'C:\Program Files\WindowsApps\OpenAI.CodexPrimaryRuntime.v26-1105-1297-0_26.1105.1297.0_x64__3k8sg7r9htsxt\dependencies\native\git\cmd\git.exe'
    $found = $candidates | Where-Object { Test-Path -LiteralPath $_ -PathType Leaf } | Select-Object -First 1
    if (-not $found) { throw 'No se encontro Git. Indica su ejecutable con -GitPath o instala Git para Windows y reabre la terminal.' }
    return $found
}

$deliveryGit = Find-DeliveryGit
if ($CheckGitOnly) {
    & $deliveryGit --no-pager --version
    if ($LASTEXITCODE -ne 0) { throw 'Git no se pudo ejecutar.' }
    return
}
$mobileRoot = [IO.Path]::GetFullPath((Split-Path -Parent $PSScriptRoot))
$reviewPath = Join-Path $mobileRoot 'artifacts\push-review\candidate.json'
if (-not (Test-Path -LiteralPath $reviewPath)) { throw 'Falta el manifiesto local de la entrega revisada.' }
$review = Get-Content -Raw -LiteralPath $reviewPath | ConvertFrom-Json
$targetBranch = $review.branch
if ($targetBranch -ne 'codex/push-google-notificaciones-20261109') { throw 'Rama de entrega no reconocida.' }

function Invoke-DeliveryGit {
    param([string[]]$GitArguments)
    & $deliveryGit --no-pager @GitArguments
    if ($LASTEXITCODE -ne 0) { throw "Git no completo: $($GitArguments[0]). Conserva el mensaje anterior." }
}

Push-Location -LiteralPath $mobileRoot
try {
    $remote = & $deliveryGit --no-pager remote get-url origin
    if ($LASTEXITCODE -ne 0 -or $remote -ne 'https://github.com/equipopatogg/Movil.git') {
        throw 'El destino debe ser equipopatogg/Movil.'
    }
    $head = & $deliveryGit --no-pager rev-parse HEAD
    if ($LASTEXITCODE -ne 0 -or $head -ne $review.base) {
        throw 'La base local cambio despues de la revision. No se publica automaticamente.'
    }
    $staged = @(& $deliveryGit --no-pager -c core.quotepath=false diff --cached --name-only)
    if ($LASTEXITCODE -ne 0) { throw 'No se pudo consultar el contenido preparado.' }

    foreach ($entry in $review.files) {
        $relative = [string]$entry.path
        $absolute = [IO.Path]::GetFullPath((Join-Path $mobileRoot $relative))
        if (-not $absolute.StartsWith($mobileRoot + '\', [StringComparison]::OrdinalIgnoreCase)) {
            throw 'El manifiesto contiene una ruta fuera del proyecto.'
        }
        if ($relative -match '(?i)(firebase-adminsdk|service.account|google-services\.json|(^|/)build/|(^|/)artifacts/|\.apk$|\.jks$|\.keystore$|(^|/)\.env)') {
            throw "Archivo excluido de entrega: $relative"
        }
        $digest = (Get-FileHash -LiteralPath $absolute -Algorithm SHA256).Hash.ToLowerInvariant()
        if ($digest -ne $entry.sha256) { throw "Cambio posterior a la revision: $relative" }
    }

    $currentBranch = & $deliveryGit --no-pager branch --show-current
    $paths = @($review.files | ForEach-Object { [string]$_.path })
    if ($staged.Count -gt 0) {
        if ($currentBranch -ne $targetBranch -or
            @(Compare-Object ($paths | Sort-Object) ($staged | Sort-Object)).Count -ne 0) {
            throw 'Hay cambios preparados ajenos a esta entrega; no se modifican.'
        }
        $raw = @(& $deliveryGit --no-pager -c core.quotepath=false diff --cached --raw --no-abbrev)
        if ($LASTEXITCODE -ne 0) { throw 'No se pudieron comprobar los archivos preparados.' }
        foreach ($line in $raw) {
            if ($line -notmatch '^:\d+ \d+ [a-f0-9]{40} ([a-f0-9]{40}) [A-Z]\t(.+)$') {
                throw 'El staging tiene un formato no previsto; no se modifica.'
            }
            $blob = $Matches[1]
            $relative = $Matches[2]
            $approved = $null
            if ($review.resumeStagedBlobHashes) {
                $approved = $review.resumeStagedBlobHashes.PSObject.Properties[$relative].Value
            }
            if ($blob -ne $approved) {
                $currentBlob = & $deliveryGit --no-pager hash-object "--path=$relative" -- $relative
                if ($LASTEXITCODE -ne 0 -or $blob -ne $currentBlob) {
                    throw "Contenido preparado no revisado: $relative"
                }
            }
        }
        Write-Host 'Se continua la entrega interrumpida; los archivos preparados fueron verificados.'
    }
    if ($CheckDeliveryOnly) {
        # The verified previous index may predate fixes; the normal path re-adds the reviewed files first.
        Invoke-DeliveryGit -GitArguments @('diff', '--check')
        Write-Host 'Entrega verificada sin crear commit ni hacer push.'
        return
    }
    if ($currentBranch -ne $targetBranch) {
        Invoke-DeliveryGit -GitArguments @('switch', '-c', $targetBranch)
    }
    $pathspecPath = Join-Path $mobileRoot 'artifacts\push-review\paths.nul'
    [IO.File]::WriteAllBytes($pathspecPath, [Text.Encoding]::UTF8.GetBytes(($paths -join [char]0) + [char]0))
    Invoke-DeliveryGit -GitArguments @('--literal-pathspecs', 'add', "--pathspec-from-file=$pathspecPath", '--pathspec-file-nul')
    $actual = @(& $deliveryGit --no-pager -c core.quotepath=false diff --cached --name-only)
    if ($LASTEXITCODE -ne 0 -or @(Compare-Object ($paths | Sort-Object) ($actual | Sort-Object)).Count -ne 0) {
        throw 'El contenido preparado no coincide con la entrega revisada. No se crea commit.'
    }
    Invoke-DeliveryGit -GitArguments @('diff', '--cached', '--check')
    Invoke-DeliveryGit -GitArguments @('-c', 'user.name=equipopatogg', '-c', 'user.email=equipopatogg@gmail.com',
        'commit', '-m', 'Integrar acceso Google y notificaciones push Android')
    Write-Host "Commit local creado. Si falla la conexion, reintenta: & '$deliveryGit' --no-pager push -u origin $targetBranch"
    Invoke-DeliveryGit -GitArguments @('push', '--set-upstream', 'origin', $targetBranch)
    Write-Host "Entrega subida a $targetBranch. No se fusiono main ni se modifico backend."
} finally { Pop-Location }

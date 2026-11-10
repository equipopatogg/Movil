param([switch]$Instrumented, [string]$Device, [string]$Python = 'python', [switch]$UseWindowsTrust, [switch]$Rebuild)
$ErrorActionPreference = 'Stop'
if ($Instrumented -and [string]::IsNullOrWhiteSpace($Device)) {
    throw 'Indica -Device con el serial de un emulador fresco y aislado. No se elige ni borra un teléfono automáticamente.'
}
$mobileRoot = Split-Path -Parent $PSScriptRoot
$bundledJdk = 'C:\Program Files\Android\Android Studio\jbr'
if (-not $env:JAVA_HOME) {
    if (Test-Path -LiteralPath (Join-Path $bundledJdk 'bin\java.exe')) {
        $env:JAVA_HOME = $bundledJdk
    } else {
        $localJdk = Get-ChildItem -LiteralPath (Join-Path $env:USERPROFILE '.jdks') -Directory -ErrorAction SilentlyContinue |
            Where-Object { $_.Name -like '*21*' -and (Test-Path -LiteralPath (Join-Path $_.FullName 'bin\java.exe')) } |
            Select-Object -First 1
        if ($localJdk) { $env:JAVA_HOME = $localJdk.FullName }
    }
}
if (-not $env:ANDROID_HOME) {
    $localSdk = Join-Path $env:LOCALAPPDATA 'Android\Sdk'
    if (Test-Path -LiteralPath $localSdk) { $env:ANDROID_HOME = $localSdk }
}
if ($Python -eq 'python') {
    $installedPython = Get-ChildItem -LiteralPath (Join-Path $env:LOCALAPPDATA 'Python') -Directory -ErrorAction SilentlyContinue |
        Where-Object { $_.Name -like 'pythoncore-*' -and (Test-Path -LiteralPath (Join-Path $_.FullName 'python.exe')) } |
        Select-Object -First 1
    if ($installedPython) { $Python = Join-Path $installedPython.FullName 'python.exe' }
}
Push-Location -LiteralPath $mobileRoot
try {
    # Opt-in for this invocation only; certificate and hostname verification remain enabled.
    $tlsOptions = @()
    if ($UseWindowsTrust) {
        if ($env:OS -ne 'Windows_NT') { throw 'UseWindowsTrust solo esta disponible en Windows.' }
        $tlsOptions = @('-Djavax.net.ssl.trustStoreType=Windows-ROOT', '-Djavax.net.ssl.trustStore=NONE')
        Write-Host 'Gradle usara los certificados de confianza de Windows para esta ejecucion.'
    }
    $rebuildOptions = @()
    if ($Rebuild) {
        $rebuildOptions = @('--rerun-tasks', '--no-build-cache')
        Write-Host 'Reconstruccion completa: se ejecutaran las tareas sin reutilizar la cache de build.'
    }
    Write-Host "JAVA_HOME: $env:JAVA_HOME"
    & .\gradlew.bat :app:assembleDebug :app:assembleDebugAndroidTest :app:createDebugUnitTestCoverageReport :app:lintDebug :app:detekt --no-configuration-cache --max-workers=1 --console=plain @tlsOptions @rebuildOptions
    if ($LASTEXITCODE -ne 0) { throw 'Falló la verificación Android.' }
    foreach ($stage in @('matrix', 'all', 'coverage', 'build')) {
        & $Python .github/scripts/android-ci-checks.py $stage
        if ($LASTEXITCODE -ne 0) { throw "Falta evidencia válida de $stage" }
    }
    & .\gradlew.bat :app:cyclonedxDirectBom --no-configuration-cache --console=plain @tlsOptions
    if ($LASTEXITCODE -ne 0) { throw 'Falló la generación del SBOM; esto no ejecuta SCA por sí solo.' }
    if ($Instrumented) {
        & $Python scripts/run-instrumented.py --device $Device
        if ($LASTEXITCODE -ne 0) { throw 'Falló el entorno o una prueba Android; sin omitir casos para obtener verde.' }
        & $Python .github/scripts/android-ci-checks.py instrumented
        if ($LASTEXITCODE -ne 0) { throw 'Falta evidencia válida de ejecución instrumentada.' }
    }
} finally { Pop-Location }

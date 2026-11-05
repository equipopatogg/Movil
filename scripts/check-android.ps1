param([switch]$Instrumented, [string]$Device, [string]$Python = 'python')
$ErrorActionPreference = 'Stop'
if ($Instrumented -and [string]::IsNullOrWhiteSpace($Device)) {
    throw 'Indica -Device con el serial de un emulador fresco y aislado. No se elige ni borra un teléfono automáticamente.'
}
$mobileRoot = Split-Path -Parent $PSScriptRoot
$bundledJdk = 'C:\Program Files\Android\Android Studio\jbr'
if (-not $env:JAVA_HOME -and (Test-Path -LiteralPath $bundledJdk)) { $env:JAVA_HOME = $bundledJdk }
Push-Location -LiteralPath $mobileRoot
try {
    & .\gradlew.bat :app:assembleDebug :app:assembleDebugAndroidTest :app:createDebugUnitTestCoverageReport :app:lintDebug :app:detekt --max-workers=1 --console=plain
    if ($LASTEXITCODE -ne 0) { throw 'Falló la verificación Android.' }
    foreach ($stage in @('matrix', 'all', 'coverage', 'build')) {
        & $Python .github/scripts/android-ci-checks.py $stage
        if ($LASTEXITCODE -ne 0) { throw "Falta evidencia válida de $stage" }
    }
    & .\gradlew.bat :app:cyclonedxDirectBom --no-configuration-cache --console=plain
    if ($LASTEXITCODE -ne 0) { throw 'Falló la generación del SBOM; esto no ejecuta SCA por sí solo.' }
    if ($Instrumented) {
        & $Python scripts/run-instrumented.py --device $Device
        if ($LASTEXITCODE -ne 0) { throw 'Falló el entorno o una prueba Android; sin omitir casos para obtener verde.' }
        & $Python .github/scripts/android-ci-checks.py instrumented
        if ($LASTEXITCODE -ne 0) { throw 'Falta evidencia válida de ejecución instrumentada.' }
    }
} finally { Pop-Location }

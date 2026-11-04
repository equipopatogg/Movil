param([switch]$Instrumented)
$ErrorActionPreference = 'Stop'
$mobileRoot = Split-Path -Parent $PSScriptRoot
$bundledJdk = 'C:\Program Files\Android\Android Studio\jbr'
if (-not $env:JAVA_HOME -and (Test-Path -LiteralPath $bundledJdk)) { $env:JAVA_HOME = $bundledJdk }
Push-Location -LiteralPath $mobileRoot
try {
    & .\gradlew.bat :app:assembleDebug :app:testDebugUnitTest :app:lintDebug :app:detekt --console=plain
    if ($LASTEXITCODE -ne 0) { throw 'Falló la verificación Android.' }
    & .\gradlew.bat :app:cyclonedxDirectBom --no-configuration-cache --console=plain
    if ($LASTEXITCODE -ne 0) { throw 'Falló la generación del SBOM.' }
    if ($Instrumented) {
        & .\gradlew.bat :app:connectedDebugAndroidTest --console=plain
        if ($LASTEXITCODE -ne 0) { throw 'Fallaron las pruebas en dispositivo.' }
    }
} finally { Pop-Location }

param()
$ErrorActionPreference = 'Stop'

# Preserve the source checkout. Build in a new, ordinary local directory.
$sourceRoot = (Resolve-Path -LiteralPath (Split-Path -Parent $PSScriptRoot)).Path
$profileRoot = [Environment]::GetFolderPath('UserProfile')
if ([string]::IsNullOrWhiteSpace($profileRoot)) { throw 'No se encontro el perfil de Windows.' }
$projectsRoot = [IO.Path]::GetFullPath((Join-Path $profileRoot 'Proyectos'))
$destination = Join-Path $projectsRoot ('PactoGG-Movil-' + (Get-Date -Format 'yyyyMMdd-HHmmss'))
if (Test-Path -LiteralPath $destination) { throw "El destino ya existe: $destination" }
if ($destination.StartsWith($sourceRoot + '\', [StringComparison]::OrdinalIgnoreCase)) {
    throw 'El destino no puede estar dentro del proyecto original.'
}
if (-not (Test-Path -LiteralPath (Join-Path $sourceRoot 'gradlew.bat'))) {
    throw 'No se encontro el proyecto Android de origen.'
}

New-Item -ItemType Directory -Path $destination -Force | Out-Null
Write-Host "Copiando el proyecto a $destination"
# Do not carry OneDrive attributes, junctions, generated files or IDE caches.
# Keep the Git checkout, source, local Android configuration and Firebase app config.
& robocopy.exe $sourceRoot $destination /E /COPY:DT /DCOPY:T /XJ /R:1 /W:1 `
    /XD build .gradle .kotlin .idea __pycache__ artifacts .guias-locales `
    /NFL /NDL /NJH /NJS /NP
if ($LASTEXITCODE -ge 8) { throw "Fallo la copia. El original esta intacto. Destino: $destination" }

$verification = Join-Path $destination 'scripts\check-android.ps1'
if (-not (Test-Path -LiteralPath $verification)) { throw 'La copia no contiene el verificador Android.' }
Write-Host "Nueva carpeta de trabajo: $destination"
Write-Host 'Ejecutando compilacion, pruebas, cobertura, Lint, Detekt y SBOM...'
Push-Location -LiteralPath $destination
try {
    & $verification -UseWindowsTrust
} catch {
    Write-Host "La verificacion no termino. Conserva el error y abre esta copia: $destination"
    throw
} finally {
    Pop-Location
}
Write-Host "Verificacion terminada. En Android Studio abre: $destination"
Write-Host 'Selecciona el emulador y pulsa Run. El proyecto original y GitHub no se modificaron.'

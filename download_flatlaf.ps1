# Script pour telecharger FlatLaf depuis GitHub
$libDir = "D:\Mes_Projets\PleinDeFoin\lib"
$flatlafUrl = "https://github.com/JFormDesigner/FlatLaf/releases/download/v3.4.1/flatlaf-3.4.1.jar"
$flatlafJar = Join-Path $libDir "flatlaf-3.4.1.jar"

Write-Host "Telechargement de FlatLaf 3.4.1..."
Write-Host "URL: $flatlafUrl"
Write-Host "Destination: $flatlafJar"

try {
    if (-not (Test-Path $libDir)) {
        New-Item -ItemType Directory -Path $libDir | Out-Null
        Write-Host "Repertoire lib cree."
    }

    $ProgressPreference = 'SilentlyContinue'
    Invoke-WebRequest -Uri $flatlafUrl -OutFile $flatlafJar -UseBasicParsing
    
    if (Test-Path $flatlafJar) {
        $size = (Get-Item $flatlafJar).Length / 1MB
        Write-Host "FlatLaf telecharge avec succes"
        exit 0
    } else {
        Write-Host "Erreur : le fichier n'a pas ete cree"
        exit 1
    }
} catch {
    Write-Host "Erreur lors du telechargement : $_"
    exit 1
}

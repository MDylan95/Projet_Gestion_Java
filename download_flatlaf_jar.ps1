$url = "https://repo1.maven.org/maven2/com/formdev/flatlaf/3.4.1/flatlaf-3.4.1.jar"
$out = "D:\Mes_Projets\PleinDeFoin\lib\flatlaf-3.4.1.jar"
[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
(New-Object Net.WebClient).DownloadFile($url, $out)
Write-Host "Telechargement termine"

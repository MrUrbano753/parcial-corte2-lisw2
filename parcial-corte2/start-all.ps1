# Script PowerShell para iniciar RabbitMQ y los 3 microservicios del Parcial Práctico Corte 2

Write-Host "==========================================================" -ForegroundColor Cyan
Write-Host " Parcial Práctico Corte 2 - Banco de Preguntas Saber PRO" -ForegroundColor Cyan
Write-Host "==========================================================" -ForegroundColor Cyan

# --- Maven: busca mvnw.cmd subiendo carpetas (raíz del repo); si no está, usa mvn del PATH ---
$mvn = $null
$dir = $PSScriptRoot
while ($dir) {
    $candidate = Join-Path $dir "mvnw.cmd"
    if ((Test-Path $candidate) -and (Test-Path (Join-Path $dir ".mvn"))) { $mvn = $candidate; break }
    $dir = Split-Path -Parent $dir
}
if (-not $mvn) {
    if (Get-Command mvn -ErrorAction SilentlyContinue) {
        $mvn = "mvn"
    } else {
        Write-Host "`nNo se encontró mvnw.cmd (con su carpeta .mvn) en ninguna carpeta superior ni 'mvn' en el PATH." -ForegroundColor Red
        Write-Host "Copie 'parcial-corte2' dentro de la raíz del repositorio (donde está mvnw.cmd) o instale Maven." -ForegroundColor Red
        exit 1
    }
}
Write-Host "Maven: $mvn" -ForegroundColor Gray

# --- JDK 21: usa la ruta de Adoptium si existe; si no, el JAVA_HOME que ya tenga el equipo ---
$jdk = "C:\Program Files\Eclipse Adoptium\jdk-21.0.12.101-hotspot"
if (Test-Path $jdk) {
    $env:JAVA_HOME = $jdk
} elseif (-not $env:JAVA_HOME) {
    Write-Host "No se encontró el JDK en '$jdk' y JAVA_HOME no está definido. Ajuste la variable `$jdk de este script." -ForegroundColor Red
    exit 1
}
Write-Host "JAVA_HOME: $env:JAVA_HOME" -ForegroundColor Gray

# 1. RabbitMQ
Write-Host "`n[1/3] Levantando RabbitMQ en Docker..." -ForegroundColor Yellow
docker compose -f (Join-Path $PSScriptRoot "docker-compose.yml") up -d

# 2. Compilar (instala shared-contracts, que usan los 3 servicios)
Write-Host "`n[2/3] Compilando el proyecto (mvn install)..." -ForegroundColor Yellow
Push-Location $PSScriptRoot
& $mvn -q clean install -DskipTests
$buildCode = $LASTEXITCODE
Pop-Location
if ($buildCode -ne 0) {
    Write-Host "`nLa compilación falló (código $buildCode). Revise el error de arriba; no se iniciarán los servicios." -ForegroundColor Red
    exit 1
}

# 3. Servicios. Orden: Lifecycle (consumidor), Validation y Question
$services = @(
    @{ Name = "Lifecycle Service";  Dir = "lifecycle-service";  Port = 8084 },
    @{ Name = "Validation Service"; Dir = "validation-service"; Port = 8082 },
    @{ Name = "Question Service";   Dir = "question-service";   Port = 8083 }
)

Write-Host "`n[3/3] Iniciando microservicios..." -ForegroundColor Yellow
foreach ($s in $services) {
    $busy = Get-NetTCPConnection -LocalPort $s.Port -State Listen -ErrorAction SilentlyContinue
    if ($busy) {
        Write-Host "El puerto $($s.Port) ya está ocupado (¿Taller 09 encendido?). Se omite $($s.Name)." -ForegroundColor Red
        continue
    }
    Write-Host "Iniciando $($s.Name) en puerto $($s.Port)..." -ForegroundColor Green
    $workingDir = Join-Path $PSScriptRoot $s.Dir
    Start-Process powershell -ArgumentList "-NoExit", "-Command", "`$env:JAVA_HOME='$($env:JAVA_HOME)'; cd '$workingDir'; & '$mvn' spring-boot:run" -WindowStyle Normal
    Start-Sleep -Seconds 5
}

Write-Host "`n==========================================================" -ForegroundColor Cyan
Write-Host " Espere a que cada ventana muestre 'Started ...Application'." -ForegroundColor White
Write-Host "  - Question Service:    http://localhost:8083/swagger-ui.html" -ForegroundColor Gray
Write-Host "  - Validation Service:  http://localhost:8082/swagger-ui.html" -ForegroundColor Gray
Write-Host "  - Lifecycle Service:   http://localhost:8084/swagger-ui.html" -ForegroundColor Gray
Write-Host "  - Consola H2:          http://localhost:8083/h2-console (jdbc:h2:mem:question_db / sa)" -ForegroundColor Gray
Write-Host "  - RabbitMQ Management: http://localhost:15672 (guest/guest)" -ForegroundColor Gray
Write-Host "==========================================================" -ForegroundColor Cyan

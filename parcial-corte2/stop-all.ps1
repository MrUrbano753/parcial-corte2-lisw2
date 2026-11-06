# Detiene los microservicios del parcial (puertos 8082, 8083, 8084) y RabbitMQ

foreach ($port in 8082, 8083, 8084) {
    $conns = Get-NetTCPConnection -LocalPort $port -State Listen -ErrorAction SilentlyContinue
    foreach ($c in $conns) {
        Write-Host "Deteniendo proceso $($c.OwningProcess) en el puerto $port" -ForegroundColor Yellow
        Stop-Process -Id $c.OwningProcess -Force -ErrorAction SilentlyContinue
    }
}
docker compose -f (Join-Path $PSScriptRoot "docker-compose.yml") down
Write-Host "Microservicios y RabbitMQ detenidos." -ForegroundColor Green

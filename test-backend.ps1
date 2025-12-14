# Quick test script to check if backend is running
Write-Host "Testing backend connection..." -ForegroundColor Yellow

try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/api/auth/register" -Method POST -ContentType "application/json" -Body '{"email":"test@test.com","password":"test1234"}' -ErrorAction Stop
    Write-Host "Backend is running!" -ForegroundColor Green
    Write-Host "Status: $($response.StatusCode)" -ForegroundColor Green
} catch {
    if ($_.Exception.Response.StatusCode -eq 400) {
        Write-Host "Backend is running! (Got 400 - expected for test request)" -ForegroundColor Green
    } elseif ($_.Exception.Response.StatusCode -eq 405) {
        Write-Host "Backend is running! (Got 405 - Method Not Allowed, which is expected)" -ForegroundColor Green
    } else {
        Write-Host "Backend is NOT accessible!" -ForegroundColor Red
        Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
        Write-Host ""
        Write-Host "Please make sure:" -ForegroundColor Yellow
        Write-Host "1. Backend is running (cd sweetshop && mvn spring-boot:run)" -ForegroundColor Yellow
        Write-Host "2. MongoDB is running" -ForegroundColor Yellow
        Write-Host "3. Port 8080 is not blocked by firewall" -ForegroundColor Yellow
    }
}

Write-Host ""
Write-Host "Press any key to exit..."
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")


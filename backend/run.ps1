$mavenVersion = "3.9.6"
$mavenDir = Join-Path $PSScriptRoot ".maven"
$mavenZip = Join-Path $PSScriptRoot "maven.zip"
$mvnCmd = Join-Path $mavenDir "apache-maven-$mavenVersion\bin\mvn.cmd"

if (-not (Test-Path $mvnCmd)) {
    Write-Host "Maven not found locally. Preparing to download..." -ForegroundColor Cyan
    if (-not (Test-Path $mavenDir)) {
        New-Item -ItemType Directory -Force -Path $mavenDir | Out-Null
    }
    
    $url = "https://archive.apache.org/dist/maven/maven-3/$mavenVersion/binaries/apache-maven-$mavenVersion-bin.zip"
    Write-Host "Downloading Maven $mavenVersion from $url..." -ForegroundColor Cyan
    
    try {
        Invoke-WebRequest -Uri $url -OutFile $mavenZip -UserAgent "Mozilla/5.0"
        Write-Host "Extracting Maven..." -ForegroundColor Cyan
        Expand-Archive -Path $mavenZip -DestinationPath $mavenDir -Force
        Remove-Item -Path $mavenZip -Force
        Write-Host "Maven set up successfully at $mavenDir" -ForegroundColor Green
    } catch {
        Write-Error "Failed to download or extract Maven. Please ensure you have internet access. Error: $_"
        exit 1
    }
}

# Verify Java is installed
try {
    $javaVer = java -version 2>&1
    Write-Host "Java detected." -ForegroundColor Green
} catch {
    Write-Error "Java (JDK) is not detected in your PATH. Please install Java 17 or higher and ensure it's in your PATH."
    exit 1
}

Write-Host "Starting Spring Boot Application..." -ForegroundColor Green
& $mvnCmd spring-boot:run

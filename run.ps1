# Script for running the Java application on Windows

Write-Host "Compiling the application..." -ForegroundColor Green

# 1. Get all libraries
$jars = Get-ChildItem -Path "librerias" -Filter *.jar
$classpath = ($jars | ForEach-Object { $_.FullName }) -join ";"

# 2. Get all Java files
$javaFiles = Get-ChildItem -Path "src" -Recurse -Filter *.java | ForEach-Object { $_.FullName }

# 3. Create build output directory
if (-not (Test-Path "build_out")) {
    New-Item -ItemType Directory -Path "build_out" | Out-Null
} else {
    Remove-Item -Path "build_out\*" -Recurse -Force -ErrorAction SilentlyContinue
}

# 4. Compile the application
javac -encoding UTF-8 -cp $classpath -d build_out $javaFiles

if ($LASTEXITCODE -ne 0) {
    Write-Error "Compilation failed!"
    exit $LASTEXITCODE
}

# 5. Copy resource files (images)
if (Test-Path "src\Imagenes") {
    Copy-Item -Path "src\Imagenes" -Destination "build_out" -Recurse -Force
}

Write-Host "Compilation successful. Running the application..." -ForegroundColor Green

# 6. Run the application
java -cp "build_out;$classpath" Vista.FrmLogin

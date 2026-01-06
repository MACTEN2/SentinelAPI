#!/bin/bash

echo "--- 🛠️  SENTINEL BUILD INITIATED ---"

# 1. Clean old builds
rm -rf out
mkdir out

# 2. Compile Java
echo "> Compiling Java source..."
javac -d out -sourcepath src/main/java src/main/java/com/sentinel/api/SentinelServer.java

if [ $? -eq 0 ]; then
    echo "> Compilation successful."
else
    echo "❌ Compilation failed. Aborting."
    exit 1
fi

# 3. Build Docker Image
echo "> Building Docker image..."
docker build -t sentinel-api:latest .

# 4. Restart Container
echo "> Restarting container..."
docker stop sentinel-server || true
docker rm sentinel-server || true
docker run -d -p 8080:8080 --name sentinel-server -v $(pwd)/logs:/app/logs sentinel-api:latest

echo "--- ✅ DEPLOYMENT COMPLETE ---"
echo "Sentinel is live at http://localhost:8080"
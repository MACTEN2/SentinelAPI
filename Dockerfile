# Use the official JDK 23 image (supports all architectures)
FROM eclipse-temurin:23-jdk

WORKDIR /app
COPY out/ /app/out/
EXPOSE 8080
CMD ["java", "-cp", "out", "com.sentinel.api.SentinelServer"]
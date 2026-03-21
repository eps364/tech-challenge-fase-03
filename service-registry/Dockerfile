FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /
COPY . .
RUN --mount=type=cache,target=/root/.m2 mvn -pl service-registry -am clean package -DskipTests

FROM eclipse-temurin:21-jre-noble
WORKDIR /app

# Security: Non-root user
RUN groupadd -r app && \
    useradd -r -g app -u 2000 -s /sbin/nologin -d /nonexistent app && \
    mkdir -p /app && chown -R app:app /app

COPY --from=builder --chown=2000:2000 /workspace/service-registry/target/*.jar /app/app.jar

EXPOSE 8762

ENTRYPOINT ["java","-jar","/app/app.jar"]

# Dev stage for hot reload (Spring Boot Devtools)
FROM maven:3.9-eclipse-temurin-21 AS dev
WORKDIR /workspace
COPY ../.. /workspace
CMD ["mvn", "spring-boot:run", "-pl", "service-registry", "-Dspring-boot.run.fork=false", "-Dspring-boot.devtools.restart.enabled=true"]

# Build stage - Maven + OpenJDK 21
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /workspace
COPY . /workspace
RUN --mount=type=cache,target=/root/.m2 mvn -pl order-service -am -B -DskipTests package

# Runtime stage - JRE 21 (menor footprint)
FROM eclipse-temurin:21-jre-noble AS runtime
WORKDIR /app

# Security: Non-root user
RUN groupadd -r app && \
    useradd -r -g app -u 2000 -s /sbin/nologin -d /nonexistent app && \
    mkdir -p /app && chown -R app:app /app

COPY --from=builder --chown=2000:2000 /workspace/order-service/target/*.jar app.jar

ENTRYPOINT ["sh","-c","exec java -jar /app/app.jar"]
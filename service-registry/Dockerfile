FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /workspace
COPY . /workspace
RUN --mount=type=cache,target=/root/.m2 mvn -pl service-registry -am clean package -DskipTests

FROM eclipse-temurin:21-jre-noble
WORKDIR /app
RUN groupadd -r app && useradd -r -g app -u 2000 app
COPY --from=builder --chown=2000:2000 /workspace/service-registry/target/*.jar /app/app.jar
USER 2000:2000
EXPOSE 8762
ENTRYPOINT ["java","-jar","/app/app.jar"]

FROM maven:3.9-eclipse-temurin-21-alpine AS build

WORKDIR /workspace

COPY backend/pom.xml ./
RUN mvn --batch-mode --no-transfer-progress dependency:go-offline

COPY backend/src ./src
RUN mvn --batch-mode --no-transfer-progress clean package -DskipTests \
    && cp target/*.jar /workspace/application.jar

FROM eclipse-temurin:21-jre-alpine AS runtime

RUN addgroup -S spring --gid 10001 \
    && adduser -S spring -G spring --uid 10001

WORKDIR /app
COPY --from=build --chown=spring:spring /workspace/application.jar /app/application.jar

USER spring
EXPOSE 8080

ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75.0 -XX:+UseG1GC"

ENTRYPOINT ["java", "-jar", "/app/application.jar"]

FROM openjdk:21-slim AS build
COPY . .
RUN ./mvnw clean package -DskipTests
#
FROM eclipse-temurin:21-jre-jammy
COPY --from=build /target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
# ---------- BUILD ----------
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /app
COPY .. .
RUN chmod +x gradlew
# ---------- RUN ----------
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/build/libs/*SNAPSHOT.jar app.jar
EXPOSE 4000
ENTRYPOINT ["java", "-jar", "app.jar"]

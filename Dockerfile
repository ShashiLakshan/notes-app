FROM ghcr.io/graalvm/graalvm-ce:ol7-java17-23.0.0
WORKDIR /app
COPY target/notes-app-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
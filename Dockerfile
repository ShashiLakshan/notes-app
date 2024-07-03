FROM ghcr.io/graalvm/graalvm-ce:ol8-java17-21.0.0.2
WORKDIR /app
COPY target/notes-app-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
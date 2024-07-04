# User Notes App

This is a Spring Boot 3.3.1 application that uses MongoDb as its database. The project utilizes Testcontainers for integration testing with a containerized MongoDb instance.

CI pipeline has been configured with GitHub : https://github.com/ShashiLakshan/notes-app/actions/workflows/maven.yml

## Prerequisites

- Java 17 or later
- Maven 3.8.1 or later
- Docker (for Testcontainers/ dev)

## Getting Started

### Clone the Repository

```bash
git clone https://github.com/ShashiLakshan/notes-app.git
cd notes-app
```
### Run MongoDB container 
- This creates MongoDb container with predefined database and a collection to run the application
```bash
cd infrastructure
docker-compose up -d
```

### Ship Docker Image
```bash
mvn package
```

### Building the Project
```bash
mvn clean install
```
### Running the Application
```bash
mvn spring-boot:run
```
The application will start and be accessible at http://localhost:8080.


## REST APIs
### Request
POST /api/v1/notes
```json
{
    "title": "Note Title",
    "content": "Note Content"
}
```
### Response
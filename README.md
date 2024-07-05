# User Notes App

This is a Spring Boot 3.3.1 application that uses MongoDb as its database. The project utilizes Testcontainers for integration testing with a containerized MongoDb instance.
User notes can be saved with Title, CreatedDate, Text and Tags such as BUSINESS, PERSONAL and IMPORTANT

CI pipeline has been configured with GitHub : https://github.com/ShashiLakshan/notes-app/actions/workflows/maven.yml

Docker Image pushed to https://hub.docker.com/repository/docker/lakshanc/notes-app/general
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

### POST /api/v1/notes
#### Request
```json
{
  "Title": "Sample Note 67",
  "CreatedDate": "2024-08-28 12:00:00",
  "Text": "This d d d d sample is a sample note.",
  "Tags": ["BUSINESS", "PERSONAL"]
}
```
#### Response
```json
{
  "Id": "66871a7abb691d0b86e47392",
  "Title": "Sample Note 67",
  "CreatedDate": "2024-08-28 12:00:00",
  "Text": "This d d d d sample is a sample note.",
  "Tags": [
    "BUSINESS",
    "PERSONAL"
  ]
}
```

### PUT /api/v1/notes
#### Request
```json
{
  "Id": "66871a7abb691d0b86e47392",
  "Title": "Updated Note"
}
```
#### Response
```json
{
  "Id": "66871a7abb691d0b86e47392",
  "Title": "Updated Note",
  "CreatedDate": "2024-08-28 12:00:00",
  "Text": "This is an updated sample note.",
  "Tags": [
    "UPDATED"
  ]
}
```

### DELETE /api/v1/notes/{id}

### GET /api/v1/notes/with-stats
#### Response
```json
[
  {
    "Id": "66871a7abb691d0b86e47392",
    "Title": "Sample Note",
    "CreatedDate": "2024-08-28 12:00:00",
    "Text": "This is a sample note.",
    "Tags": [
      "BUSINESS",
      "PERSONAL"
    ],
    "Statistics": {
      "WordCount": 6,
      "CharacterCount": 28
    }
  }
]
```

### GET /api/v1/notes?tags=BUSINESS,PERSONAL&page=0&size=2&sortDirection=DESC
#### Response
```json
{
  "content": [
    {
      "Id": "66871a7abb691d0b86e47393",
      "Title": "Sample Note 2",
      "CreatedDate": "2024-08-29 12:00:00",
      "Text": "This is a sample note 2.",
      "Tags": ["BUSINESS"]
    },
    {
      "Id": "66871a7abb691d0b86e47392",
      "Title": "Sample Note 1",
      "CreatedDate": "2024-08-28 12:00:00",
      "Text": "This is a sample note 1.",
      "Tags": ["BUSINESS", "PERSONAL"]
    }
  ],
  "page": 0,
  "size": 2,
  "totalElements": 5
}
```

### GET /api/v1/notes/{id}
#### Response
```json
{
  "Id": "66871a7abb691d0b86e47392",
  "Title": "Sample Note",
  "CreatedDate": "2024-08-28 12:00:00",
  "Text": "This is a sample note.",
  "Tags": [
    "BUSINESS",
    "PERSONAL"
  ]
}

```
# REXO AI - System Architecture

## Architecture Overview

REXO AI follows a Client-Server Architecture.

The Android application acts as the client.

The Python APIs act as the server.

Communication occurs through HTTP requests using JSON data.

---

## High-Level Architecture

```text
Android Application
        │
        ▼
    Retrofit
        │
        ▼
     OkHttp
        │
        ▼
   HTTP Request
        │
        ▼
  Python Flask API
        │
        ▼
Business Logic Layer
        │
        ▼
FAQ Processing
Jarvis Processing
SQL Detection
        │
        ▼
 JSON Response
        │
        ▼
Android Application
```

---

## Frontend Layer

The Android application is responsible for:

* User interaction
* Input collection
* Displaying responses
* Managing API requests

Components:

* Activities
* RecyclerView
* Retrofit
* Gson
* OkHttp

---

## Backend Layer

The backend consists of Python APIs.

Responsibilities:

* Processing user requests
* Searching FAQ datasets
* Executing utility commands
* Detecting SQL injection patterns
* Returning responses

---

## Communication Layer

Communication occurs using:

* HTTP Protocol
* REST APIs
* JSON Payloads

Example Request:

```json
{
  "message": "What is hostel fee?"
}
```

Example Response:

```json
{
  "response": "The hostel fee is ..."
}
```

---

## Jack AI Architecture

```text
User Query
    │
    ▼
Android App
    │
    ▼
Jack API
    │
    ▼
FAQ Dataset
    │
    ▼
Best Match Search
    │
    ▼
Response Returned
```

---

## Jarvis AI Architecture

```text
User Command
      │
      ▼
Android App
      │
      ▼
Jarvis API
      │
      ▼
Command Processing
      │
      ▼
Response Generation
      │
      ▼
Return Result
```

---

## SQL Guard Architecture

```text
User File
     │
     ▼
Android App
     │
     ▼
SQL API
     │
     ▼
Pattern Analysis
     │
     ▼
Threat Detection
     │
     ▼
Security Report
```

---

## Design Advantages

### Separation of Concerns

Frontend and backend remain independent.

### Scalability

Backend services can be expanded independently.

### Maintainability

Each module performs a specific task.

### Security

Sensitive processing remains on the backend.

### Reusability

APIs can be reused by future applications.

---

## Architecture Summary

REXO AI follows a modular client-server architecture where Android handles user interaction and Python APIs handle processing logic. This design improves maintainability, scalability, and future extensibility.

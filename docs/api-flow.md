# REXO AI - API Flow Documentation

## Overview

REXO AI uses REST APIs to communicate between the Android application and Python backend services.

Data is exchanged using JSON format.

---

## Request Flow

```text
User Input
      │
      ▼
Android UI
      │
      ▼
Java Object
      │
      ▼
Retrofit
      │
      ▼
JSON Request
      │
      ▼
OkHttp
      │
      ▼
HTTP Network
      │
      ▼
Python API
```

---

## Response Flow

```text
Python Response
       │
       ▼
JSON Response
       │
       ▼
HTTP Network
       │
       ▼
OkHttp
       │
       ▼
Retrofit
       │
       ▼
Gson
       │
       ▼
Java Object
       │
       ▼
RecyclerView
       │
       ▼
User Interface
```

---

## Jack AI API

### Endpoint

```http
POST /jack/command
```

### Request

```json
{
  "message": "What is hostel fee?"
}
```

### Response

```json
{
  "response": "Hostel fee details..."
}
```

---

## Jarvis AI API

### Endpoint

```http
POST /jarvis/command
```

### Request

```json
{
  "command": "tell me a joke"
}
```

### Response

```json
{
  "response": "Sample joke..."
}
```

---

## SQL Guard API

### Endpoint

```http
POST /sql/scan_query
```

### Request

```json
{
  "query": "SELECT * FROM users"
}
```

### Response

```json
{
  "safe": true,
  "message": "No SQL injection detected"
}
```

---

## Communication Components

### Retrofit

Creates API calls.

### OkHttp

Handles network communication.

### JSON

Transfers data.

### Flask

Processes requests.

### Gson

Converts JSON to Java objects.

### RecyclerView

Displays responses.

---

## Advantages

* Lightweight communication
* Platform independent
* Easy debugging
* Scalable architecture
* Easy frontend-backend separation

---

## Summary

REXO AI follows a REST-based communication architecture where Android clients exchange JSON messages with Python backend APIs using Retrofit and OkHttp.

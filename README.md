# REXO AI

An AI-powered Android assistant application developed using Android Studio, Java, XML, and Python APIs. REXO AI combines multiple intelligent modules into a single platform, providing educational assistance, utility automation, and cybersecurity analysis capabilities.

---

# Overview

REXO AI is a modular Android application designed to demonstrate full-stack mobile development through the integration of Android frontend technologies and Python backend APIs.

The application consists of three independent modules:

* Jack AI (GNDEC Query Assistant)
* Jarvis AI (Utility Assistant)
* SQL Injection Detection (SQL Guard)

Each module communicates with dedicated backend APIs using REST architecture and JSON-based data exchange.

---

# Features

## Jack AI – GNDEC Query Assistant

Jack AI is a college information assistant developed to answer GNDEC-related queries.

### Capabilities

* Hostel information
* Admission guidance
* Department information
* College facilities
* Academic information
* Frequently asked questions

### Example Queries

```text
What is the hostel fee?

What courses are offered?

Where is the library located?
```

---

## Jarvis AI – Smart Utility Assistant

Jarvis AI performs utility-based tasks and command processing.

### Features

* Mathematical calculations
* Joke generation
* Utility commands
* YouTube search assistance
* General assistant operations

### Example Commands

```text
Calculate 25 + 15

Tell me a joke

Open YouTube
```

---

## SQL Guard – SQL Injection Detection

SQL Guard is a cybersecurity-focused module that helps identify potentially malicious SQL statements.

### Features

* SQL query scanning
* File analysis
* Threat detection
* Security reporting
* Pattern-based analysis

### Example

```sql
SELECT * FROM users WHERE username='admin' OR '1'='1'
```

The module analyzes the query and reports whether suspicious SQL injection patterns are detected.

---

# Technology Stack

## Frontend

* Android Studio
* Java
* XML
* RecyclerView
* Retrofit
* OkHttp
* Gson

## Backend

* Python
* Flask APIs
* JSON Processing

## Deployment

* Render

## Version Control

* GitHub

---

# Project Architecture

The project follows a Client-Server Architecture.

```text
Android User
      │
      ▼
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
Python Backend API
      │
      ▼
Business Logic
      │
      ▼
JSON Response
      │
      ▼
Android Application
      │
      ▼
RecyclerView
```

---

# Repository Structure

```text
REXO-AI/
│
├── android-app/
│
├── backend/
│
├── docs/
│
├── screenshots/
│
├── sample-files/
│
├── README.md
│
└── .gitignore
```

---

# Installation

## Clone Repository

```bash
git clone https://github.com/yourusername/REXO-AI.git
```

---

## Backend Setup

Navigate to backend folder:

```bash
cd backend
```

Create virtual environment:

```bash
python -m venv venv
```

Activate environment:

### Windows

```bash
venv\Scripts\activate
```

### Linux / Mac

```bash
source venv/bin/activate
```

Install dependencies:

```bash
pip install -r requirements.txt
```

---

# Running Backend Services

## Jack AI

```bash
python jack_api.py
```

---

## Jarvis AI

```bash
python jarvis_api.py
```

---

## SQL Guard

```bash
python sql_api.py
```

---

# Android Setup

1. Open Android Studio
2. Select Open Existing Project
3. Open android-app folder
4. Wait for Gradle synchronization
5. Run the application

---

# API Communication

The application uses:

* Retrofit for API creation
* OkHttp for network communication
* Gson for JSON serialization/deserialization

Data is exchanged between Android and Python services using JSON payloads.

---

# Screenshots

Add screenshots inside:

```text
screenshots/
```

Recommended screenshots:

* Home Screen
* Jack AI Interface
* Jarvis AI Interface
* SQL Guard Interface
* Results Screen

Example:

```markdown
![Home Screen](screenshots/home_screen.png)

![Jack AI](screenshots/jack_ai.png)

![Jarvis AI](screenshots/jarvis_ai.png)

![SQL Guard](screenshots/sql_guard.png)
```

---

# Future Improvements

Potential future enhancements include:

* Voice-based interaction
* Large Language Model integration
* Chat history management
* User authentication
* Cloud database integration
* Multilingual support
* Real-time internet search
* Enhanced SQL threat analysis
* AI-powered recommendations

---

# Learning Outcomes

Through this project the following concepts were implemented and explored:

* Android Development
* Java Programming
* REST API Integration
* JSON Communication
* Retrofit
* OkHttp
* Backend Development
* Flask APIs
* Cybersecurity Concepts
* Full Stack Mobile Development

---

# Documentation

Detailed documentation is available inside:

```text
docs/
```

Including:

* Project Overview
* System Architecture
* API Flow
* Setup Guide
* Deployment Guide

---

# Author

**Vaibhav**

B.Tech Information Technology

Areas of Interest:

* Software Development
* Android Development
* Artificial Intelligence
* Backend Engineering
* Cybersecurity

---

# License

This project is intended for educational and learning purposes.

You may modify and extend the project for academic use.

---

# Acknowledgements

Special thanks to:

* Android Studio
* Python Community
* Flask Framework
* Retrofit
* OkHttp
* Open Source Community

for providing the tools and technologies used in the development of this project.

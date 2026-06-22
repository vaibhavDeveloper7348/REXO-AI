# REXO AI - Setup Guide

## Introduction

This guide explains how to set up and run the REXO AI project on a local machine.

The project consists of:

* Android Frontend (Java)
* Python Backend APIs

---

## Software Requirements

### Frontend

* Android Studio Hedgehog or later
* Android SDK
* Java JDK 17 or later

### Backend

* Python 3.10 or later
* pip package manager

---

## Clone Repository

Download or clone the repository:

```bash
git clone https://github.com/yourusername/REXO-AI.git
```

Project structure:

```text
REXO-AI/
│
├── android-app/
├── backend/
├── docs/
└── README.md
```

---

## Backend Setup

Navigate to backend directory:

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

### Linux/Mac

```bash
source venv/bin/activate
```

Install dependencies:

```bash
pip install -r requirements.txt
```

---

## Start Backend Services

### Jack AI

```bash
python jack_api.py
```

Runs on:

```text
http://localhost:5001
```

---

### Jarvis AI

```bash
python jarvis_api.py
```

Runs on:

```text
http://localhost:5002
```

---

### SQL Guard

```bash
python sql_api.py
```

Runs on:

```text
http://localhost:5003
```

---

## Android Setup

Open Android Studio.

Select:

```text
Open Existing Project
```

Choose:

```text
android-app/
```

Wait for:

* Gradle Sync
* SDK Installation

to complete.

---

## Configure API URLs

Locate API configuration files.

Replace API URLs with:

```text
http://10.0.2.2:5001
```

for Jack AI

```text
http://10.0.2.2:5002
```

for Jarvis AI

```text
http://10.0.2.2:5003
```

for SQL Guard

Android Emulator uses:

```text
10.0.2.2
```

instead of localhost.

---

## Run Application

Start backend APIs.

Then click:

```text
Run → Run App
```

inside Android Studio.

The application should install automatically on the emulator or connected Android device.

---

## Verification

Verify:

✓ App launches successfully

✓ Jack AI responds

✓ Jarvis AI responds

✓ SQL Guard scans files

✓ API communication works

---

## Troubleshooting

### API Connection Error

Verify backend server is running.

---

### Retrofit Timeout

Check network connectivity.

---

### Module Not Responding

Verify correct API port numbers.

---

### Dependency Issues

Run:

```bash
pip install -r requirements.txt
```

again.

---

## Setup Complete

The REXO AI application is now ready for local development and testing.

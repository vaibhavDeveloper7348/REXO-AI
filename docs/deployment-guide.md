# REXO AI - Deployment Guide

## Overview

The backend APIs are deployed on Render Cloud Platform.

Deployment allows the Android application to communicate with publicly accessible APIs.

---

## Deployment Platform

Platform:

```text
Render.com
```

Benefits:

* Free hosting tier
* Automatic deployment
* HTTPS support
* Easy GitHub integration

---

## Prepare Backend

Ensure backend contains:

```text
requirements.txt
```

Example:

```text
Flask
flask-cors
gunicorn
```

---

## Create Render Account

1. Visit Render.com
2. Create account
3. Connect GitHub account

---

## Create New Web Service

Select:

```text
New Web Service
```

Choose:

```text
REXO-AI Backend Repository
```

---

## Build Command

```bash
pip install -r requirements.txt
```

---

## Start Command

Example:

```bash
python jack_api.py
```

or

```bash
gunicorn app:app
```

depending on deployment configuration.

---

## Environment Variables

Store sensitive information as environment variables.

Never hardcode:

* API keys
* Tokens
* Passwords

---

## Deploy

Click:

```text
Create Web Service
```

Render automatically deploys the backend.

---

## Deployment Verification

Verify:

```text
https://your-api.onrender.com/health
```

returns:

```json
{
  "status": "running"
}
```

---

## Android Configuration

Replace localhost URLs with Render URLs.

Example:

```text
https://your-api.onrender.com
```

---

## Updating Deployment

Whenever new code is pushed:

```text
GitHub → Render
```

Render automatically redeploys.

---

## Security Recommendations

* Hide API keys
* Enable HTTPS
* Validate inputs
* Sanitize user data

---

## Deployment Complete

Backend APIs are now accessible from any Android device connected to the internet.

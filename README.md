# 📱 Social Media App

Eine moderne Social Media Anwendung mit Spring Boot und JWT-Authentifizierung.

## 🚀 Features

- **Benutzerregistrierung & Anmeldung** mit JWT-Token
- **Posts erstellen, bearbeiten & löschen**
- **Kommentare** zu Posts hinzufügen
- **Like-System** für Posts
- **REST API** mit vollständiger CRUD-Funktionalität

## 🛠️ Technologien

- **Backend**: Spring Boot 3.0.2, Java 17
- **Datenbank**: MySQL 8.0
- **Sicherheit**: Spring Security + JWT
- **Build Tool**: Maven
- **Container**: Docker & Docker Compose

## 📋 Voraussetzungen

- Java 17
- MySQL 8.0
- Maven 3.6+
- Docker (optional)

## 🔧 Installation & Start

### Lokal starten:
```bash
# Repository klonen
git clone https://github.com/OubeidHanini/social-media-app.git
cd social-media-app

# .env Datei konfigurieren (siehe .env.example)
cp .env.example .env

# Anwendung starten
./mvnw spring-boot:run
```

### Mit Docker starten:
```bash
# Docker Container starten
docker-compose up --build

# Im Hintergrund starten
docker-compose up -d
```

## 🌐 API Endpoints

| Methode | Endpoint | Beschreibung |
|---------|----------|--------------|
| POST | `/auth/register` | Benutzerregistrierung |
| POST | `/auth/login` | Benutzeranmeldung |
| GET | `/posts` | Alle Posts abrufen |
| POST | `/posts` | Neuen Post erstellen |
| POST | `/comments` | Kommentar hinzufügen |
| POST | `/likes` | Post liken |

## 📝 Beispiel API Requests

### Registrierung:
```json
POST /auth/register
{
  "username": "testuser",
  "password": "password123"
}
```

### Post erstellen:
```json
POST /posts
{
  "title": "Mein Post",
  "text": "Post Inhalt",
  "userId": 1
}
```

## ⚙️ Konfiguration

Die Anwendung läuft standardmäßig auf:
- **Port**: 8080
- **MySQL**: localhost:3306 (lokal) oder 13306 (Docker)

## 🔐 Umgebungsvariablen

Konfiguriert über `.env` Datei:
```env
MYSQL_ROOT_PASSWORD=your_password
MYSQL_DATABASE=question
QUESTION_APP_SECRET=your_jwt_secret
```

## 🚀 Entwickelt von

[OubeidHanini](https://github.com/OubeidHanini)

---
⭐ Vergiss nicht, dem Projekt einen Stern zu geben!
# 🐳 Docker-Konfiguration - Social Media App

## Docker-Befehle

### 1. Anwendung erstellen und starten
```bash
docker-compose up --build
```

### 2. Im Hintergrund starten
```bash
docker-compose up -d
```

### 3. Anwendung stoppen
```bash
docker-compose down
```

### 4. Logs anzeigen
```bash
docker-compose logs -f spring-app
docker-compose logs -f mysql-db
```

### 5. Nur App neu erstellen
```bash
docker-compose build spring-app
docker-compose up spring-app
```

## Verfügbare Endpunkte

Nach dem Start ist die Anwendung erreichbar unter:
- **Anwendung**: http://localhost:8080
- **Datenbank**: localhost:3306

## Konfiguration

### Umgebungsvariablen
- `MYSQL_ROOT_PASSWORD`: aus .env-Datei
- `MYSQL_DATABASE`: question
- `SPRING_PROFILES_ACTIVE`: docker

### Ports
- **Spring Boot**: 8080
- **MySQL**: 3306

### Volumes
- `mysql_data`: Persistiert MySQL-Daten

## Fehlerbehebung

### Wenn MySQL nicht startet
```bash
docker-compose down -v
docker-compose up --build
```

### Container-Zugriff
```bash
docker exec -it social-media-app bash
docker exec -it social-media-mysql mysql -u root -p
```
FROM eclipse-temurin:17-jdk-jammy

# Maven installieren
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

# Arbeitsverzeichnis erstellen
WORKDIR /app

# Quellcode komplett kopieren
COPY . .

# Anwendung mit Maven erstellen
RUN mvn clean package -DskipTests

# Port 8080 freigeben
EXPOSE 8080

# Befehl zum Starten der Anwendung
ENTRYPOINT ["java", "-jar", "target/questionapp-0.0.1-SNAPSHOT.jar"]
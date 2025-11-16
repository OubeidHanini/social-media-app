package com.example.questionapp;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class QuestionappApplication {

	public static void main(String[] args) {
		// Umgebungsvariablen aus der .env-Datei laden
		Dotenv dotenv = Dotenv.configure().load();
		dotenv.entries().forEach(entry -> 
			System.setProperty(entry.getKey(), entry.getValue())
		);
		
		SpringApplication.run(QuestionappApplication.class, args);
	}

}

package com.example.report;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReportApplication {

	public static void main(String[] args) {
		// Disable Axon update checker and console messages
		System.setProperty("disable-axoniq-console-message", "true");
		System.setProperty("axon.update.check.enabled", "false");
		
		SpringApplication.run(ReportApplication.class, args);
	}

}

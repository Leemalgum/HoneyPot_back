package com.beeSpring.beespring;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableAdminServer
@EnableScheduling
public class BeespringApplication {
	public static void main(String[] args) {

		SpringApplication.run(BeespringApplication.class, args);
	}

}

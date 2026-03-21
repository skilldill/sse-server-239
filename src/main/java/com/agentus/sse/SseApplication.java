package com.agentus.sse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
public class SseApplication {

	public static void main(String[] args) {
		SpringApplication.run(SseApplication.class, args);
	}

}

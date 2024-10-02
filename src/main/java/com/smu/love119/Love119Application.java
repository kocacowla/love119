package com.smu.love119;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Love119Application {

	public static void main(String[] args) {
		SpringApplication.run(Love119Application.class, args);
	}

}

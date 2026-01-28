package com.paysplit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PaysplitApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaysplitApplication.class, args);
	}

}

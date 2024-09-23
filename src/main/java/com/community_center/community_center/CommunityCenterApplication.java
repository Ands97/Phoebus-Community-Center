package com.community_center.community_center;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class CommunityCenterApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommunityCenterApplication.class, args);
	}

}

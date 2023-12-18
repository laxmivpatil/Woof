package com.example.techverse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WoofApplication {

	public static void main(String[] args) {
		SpringApplication.run(WoofApplication.class, args);
	}

	
	/*
	 * 
	 spring.datasource.url=jdbc:mysql://localhost:3306/woof_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root


#spring.datasource.url=jdbc:mysql://satya.c0u2az8dkbnk.us-east-1.rds.amazonaws.com:3306/woof_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
#spring.datasource.username=root
#spring.datasource.password=root1234

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
	 */
}

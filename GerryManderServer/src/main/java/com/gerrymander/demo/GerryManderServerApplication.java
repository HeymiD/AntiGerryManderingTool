package com.gerrymander.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;
//import org.hibernate.cfg.AnnotationConfiguration;
//import org.hibernate.Query;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;

@SpringBootApplication
public class GerryManderServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(GerryManderServerApplication.class, args);
	}

}

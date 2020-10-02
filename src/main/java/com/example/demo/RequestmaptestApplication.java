package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class RequestmaptestApplication {
    public static void main(String[] args) {
	    ApplicationContext applicationContext = SpringApplication.run(RequestmaptestApplication.class, args);
		SyncTask sync = applicationContext.getBean(SyncTask.class);
		sync.configureTasks();
	}
}


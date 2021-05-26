package com.carlosmax.docprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@SpringBootApplication
public class DocprocessorApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(DocprocessorApplication.class, args);
	}

}

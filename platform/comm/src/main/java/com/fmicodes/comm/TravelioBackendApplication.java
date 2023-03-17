package com.fmicodes.comm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class TravelioBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(TravelioBackendApplication.class, args);
	}

}

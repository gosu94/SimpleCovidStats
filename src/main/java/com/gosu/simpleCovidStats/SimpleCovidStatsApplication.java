package com.gosu.simpleCovidStats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.gosu")
public class SimpleCovidStatsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleCovidStatsApplication.class, args);
	}

}

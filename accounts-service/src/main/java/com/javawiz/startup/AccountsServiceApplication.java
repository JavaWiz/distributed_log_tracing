package com.javawiz.startup;

import brave.sampler.Sampler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import com.javawiz.config.AccountsConfiguration;

@SpringBootApplication
@EnableEurekaClient
@Import(AccountsConfiguration.class)
public class AccountsServiceApplication {

	public static void main(String[] args) {
		// Tell Boot to look for registration-server.yml
	    System.setProperty("spring.config.name", "accounts-service");
		SpringApplication.run(AccountsServiceApplication.class, args);
	}

	@Bean
	public Sampler defaultSampler() {
		return Sampler.ALWAYS_SAMPLE;
	}
}

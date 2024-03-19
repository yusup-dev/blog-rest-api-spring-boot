package com.springboot.blog;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.ui.Model;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Spring Boot Blog App REST APIs",
				description = "Spring Boot App REST APIs Documentation",
				version = "v1.0",
				contact = @Contact(
						name = "Muhamad Yusup",
						email = "mhmdy5p0317@gmail.com",
						url = "https://www.yusup.tech"
				),
				license = @License(
						name = "Apache 2.0",
						url = "https://www.yusup.tech"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "Spring Boot Blog App Documentation",
				url = "https://github.com/yusup-dev/springboot-blog-rest-api"
		)
)
public class RestApiBlogApplication {

	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}

	public static void main(String[] args) {
		SpringApplication.run(RestApiBlogApplication.class, args);
	}

}

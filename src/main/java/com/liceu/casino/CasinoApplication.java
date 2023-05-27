package com.liceu.casino;

import com.liceu.casino.interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class CasinoApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(CasinoApplication.class, args);
	}

	@Autowired
	TokenInterceptor tokenInterceptor;
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(tokenInterceptor)
				.addPathPatterns("/login")
				.addPathPatterns("/c/**");
	}
}

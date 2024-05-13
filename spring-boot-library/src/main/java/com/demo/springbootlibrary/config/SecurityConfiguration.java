package com.demo.springbootlibrary.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;

import com.okta.spring.boot.oauth.Okta;

@Configuration
public class SecurityConfiguration {

//	@Bean
//	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
//		http.csrf(csrf-> csrf.disable());
//		
//		http.authorizeHttpRequests(customizer -> customizer.requestMatchers("/api/books/secure/**").authenticated()).oauth2ResourceServer().jwt();
//		
//		http.cors();
//		
//		http.setSharedObject(ContentNegotiationStrategy.class, new HeaderContentNegotiationStrategy());
//		
//		Okta.configureResourceServer401ResponseBody(http);
//		
//		return http.build();

//	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests(authorizeRequests -> authorizeRequests
				.requestMatchers("/api/books/secure/**", "/api/reviews/secure/**,"
						+ " /api/messages/secure/**, /api/admin/secure/**", "/api/payment/secure/**")
				.authenticated().anyRequest().permitAll())
				.oauth2ResourceServer().jwt();

		http.cors();

		http.setSharedObject(ContentNegotiationStrategy.class, new HeaderContentNegotiationStrategy());

		Okta.configureResourceServer401ResponseBody(http);

		return http.build();
	}
}

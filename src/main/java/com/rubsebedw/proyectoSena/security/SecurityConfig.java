package com.rubsebedw.proyectoSena.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
	SecurityFilterChain SecurityfilterChain(HttpSecurity http) throws Exception {
		http.formLogin();
		http.authorizeHttpRequests().anyRequest().authenticated();
		return http.build();
		}
	
	@Bean
	UserDetailsService userDetailsService() {
		InMemoryUserDetailsManager userDetailsService = new InMemoryUserDetailsManager();
		UserDetails user = User.withUsername("sebastian").password(passwordEnconder().encode("123")).authorities("read").build();
		UserDetails user2 = User.withUsername("ruben").password(passwordEnconder().encode("123")).authorities("read").build();
		userDetailsService.createUser(user);
		userDetailsService.createUser(user2);
		return userDetailsService;
	}
	
	@Bean
	BCryptPasswordEncoder passwordEnconder() {
		return new BCryptPasswordEncoder();
	}
	
////	@SuppressWarnings("deprecation")
////	@Bean
////	public InMemoryUserDetailsManager userDetailsService() {
////		
////		UserDetails user = User.withDefaultPasswordEncoder()
////				.username("sebastian")
////				.password("123")
////				.roles("ADMIN","USER")
////				.build();
////		return new InMemoryUserDetailsManager(user);				
////	}
//	
////	@Bean
////	public SecurityFilterChain apiSecurityfilterChain(HttpSecurity http) throws Exception {
////		return http
////				.securityMatcher("/**")
////				.authorizeHttpRequests(auth -> {
////					auth.anyRequest().authenticated();
////				})
////				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
////				.httpBasic()				
////				.build();
////	}
////	
////	@Bean
////	public SecurityFilterChain h2ConsoleSecurityFilterChain(HttpSecurity http) throws Exception {
////		return http
////				.securityMatcher(AntPathRequestMatcher.antMatcher("/h2-console/**"))
////				.authorizeHttpRequests( auth -> {
////					auth.requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll();
////				})
////				.csrf(csrf -> csrf.ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")))
////				.headers(headers -> headers.frameOptions().disable())				
////				.build();
////		
////	}
}


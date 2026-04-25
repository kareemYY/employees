package com.luv2code.springboot.employees.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {


    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        UserDetails kareem = User.builder()
                .username("kareem")
                .password("{noop}123")
                .roles("EMPLOYEE")
                .build();
        UserDetails aser = User.builder()
                .username("aser")
                .password("{noop}123")
                .roles("EMPLOYEE","MANAGER")
                .build();
        UserDetails sara = User.builder()
                .username("sara")
                .password("{noop}123")
                .roles("EMPLOYEE","MANAGER","ADMIN")
                .build();
        return new InMemoryUserDetailsManager(kareem, aser, sara);
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(configurer ->
                configurer
                        .requestMatchers("/docs/**","/swagger-ui/**","/v3/api-docs/**","/swagger-ui.html").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/employees").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.GET,"/api/employees/**").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.POST,"/api/employees").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.PUT,"/api/employees/**").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.DELETE,"/api/employees/**").hasRole("ADMIN")

        );
        http.httpBasic(httpBasicCustomizer ->httpBasicCustomizer.disable());
        http.httpBasic(Customizer.withDefaults());

        http.csrf(csrf -> csrf.disable());

        http.exceptionHandling(exception ->
         exception.authenticationEntryPoint(authenticationEntryPoint()));
        return http.build();
    }


    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint(){
        return (request, response, authException) ->{
                response.setStatus(HttpStatus.UNAUTHORIZED.value()) ;
                response.setContentType("application/json");
                response.setHeader("WWW-Authenticate","");
                response.getWriter().write("{\"error\":\"Unauthorized access\"}");
        };
}
















}

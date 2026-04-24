package com.luv2code.springboot.employees;

import com.luv2code.springboot.employees.mapper.EmployeeMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EmployeesApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeesApplication.class, args);
	}

	@Bean
	public EmployeeMapper employeeMapper() {
		return new EmployeeMapper();
	}
}

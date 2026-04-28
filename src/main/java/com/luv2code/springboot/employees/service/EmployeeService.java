package com.luv2code.springboot.employees.service;

import com.luv2code.springboot.employees.Dto.EmployeeDto;
import com.luv2code.springboot.employees.entity.Employee;

import java.util.List;

public interface EmployeeService {

     List<EmployeeDto> findAll();
     EmployeeDto findEmployeeById(long id);
     EmployeeDto updateEmployee(long id,EmployeeDto employee);
     void deleteEmployee(long  id);
    EmployeeDto createEmployee(EmployeeDto employee);
    boolean existsEmployeeByEmail(String email);
}

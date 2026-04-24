package com.luv2code.springboot.employees.mapper;

import com.luv2code.springboot.employees.Dto.EmployeeDto;
import com.luv2code.springboot.employees.entity.Employee;

import java.util.ArrayList;
import java.util.List;

public class EmployeeMapper {

    public EmployeeDto mapEmployeeToEmployeeDto(Employee employee) {
      return new EmployeeDto(
              employee.getId(),
              employee.getFirstName(),
              employee.getLastName(),
              employee.getEmail()
      );
    }
    public Employee mapEmployeeDtoToEmployee(EmployeeDto employeeDto) {
        return new Employee(
                employeeDto.getId(),
                employeeDto.getFirstName(),
                employeeDto.getLastName(),
                employeeDto.getEmail()
        );
    }

    public Employee updateEmployee(Employee employee,EmployeeDto employeeDto) {
       employee.setFirstName(employeeDto.getFirstName());
       employee.setLastName(employeeDto.getLastName());
       employee.setEmail(employeeDto.getEmail());
        return employee;
    }

    public List<EmployeeDto> mapEmployeeToEmployeeDtoList(List<Employee> employees) {
        List<EmployeeDto> employeeDtoList = new ArrayList<>();
        employees.forEach(employee -> employeeDtoList.add(mapEmployeeToEmployeeDto(employee)));
        return employeeDtoList;
    }
  public List<Employee>  mapEmployeeDtoListToEmployees(List<EmployeeDto> employeeDtoList) {
        List<Employee> employeeList = new ArrayList<>();
        employeeDtoList.forEach(employeeDto -> employeeList.add(mapEmployeeDtoToEmployee(employeeDto)));
        return employeeList;
  }

}

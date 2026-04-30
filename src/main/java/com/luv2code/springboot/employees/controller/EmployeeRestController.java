package com.luv2code.springboot.employees.controller;


import com.luv2code.springboot.employees.Dto.EmployeeDto;
import com.luv2code.springboot.employees.entity.Employee;
import com.luv2code.springboot.employees.service.EmployeeService;
import com.luv2code.springboot.employees.service.EmployeeServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "Employee Rest API End Point",description = "Operation related to Employees")
@RestController
@RequestMapping("/api/employees")
public class EmployeeRestController {

    private EmployeeService employeeService;

    @Autowired
    public EmployeeRestController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }


    @Operation(summary = "Get All Employees",description = "retrieve all employees form database")
    @GetMapping()
    public List<EmployeeDto> findAllEmployees() {
        return employeeService.findAll();
    }


    @Operation(summary = "Get Employee By Id ")
    @GetMapping("/{employeeId}")
    @ResponseStatus(HttpStatus.OK)
    public EmployeeDto findById(@Parameter(description = "Id of employee")
                                    @PathVariable @Min(value = 1) int employeeId) {
        return employeeService.findEmployeeById(employeeId);
    }

    @Operation(summary = "Create an Employee", description = "add a new employee to database")
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeDto createEmployee(@Valid @RequestBody EmployeeDto employeeDto) {
        return employeeService.createEmployee(employeeDto);
    }

    @Operation(summary = "Update employee",description = "update Data for employee")
    @PutMapping("/{employeeId}")
    @ResponseStatus(HttpStatus.OK)
    public EmployeeDto updateEmployee(@Parameter(description = "Id of employee")
                                          @PathVariable int employeeId,  @Valid @RequestBody EmployeeDto employeeDto ) {
        return employeeService.updateEmployee(employeeId, employeeDto);
    }

    @Operation(summary = "Delete Employee ",description = "delete employee from database")
    @DeleteMapping("/{employeeId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteEmployee(@Parameter(description = "Id of employee")
                                   @PathVariable @Min(value = 1) int employeeId) {
        employeeService.deleteEmployee(employeeId);

    }


























}

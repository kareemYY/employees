package com.luv2code.springboot.employees.service;

import com.luv2code.springboot.employees.Dto.EmployeeDto;
import com.luv2code.springboot.employees.employeeDao.EmployeeDao;
import com.luv2code.springboot.employees.entity.Employee;
import com.luv2code.springboot.employees.exception.EmployeeNotFoundException;
import com.luv2code.springboot.employees.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class EmployeeServiceImpl  implements EmployeeService {

    private final EmployeeDao employeeDao;
    private final EmployeeMapper employeeMapper;

    @Autowired
    public EmployeeServiceImpl(EmployeeDao employeeDao, EmployeeMapper employeeMapper) {
        this.employeeDao = employeeDao;
        this.employeeMapper = employeeMapper;
    }

    @Override
    public List<EmployeeDto> findAll() {
        return employeeMapper.mapEmployeeToEmployeeDtoList(employeeDao.findAll());
    }

    @Override
    public EmployeeDto findEmployeeById(long id) {
        return employeeMapper.mapEmployeeToEmployeeDto(employeeDao.findById(id).
                orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id " + id)));
    }

    @Transactional
    @Override
    public EmployeeDto updateEmployee(long id,EmployeeDto employeeDto) {
      Employee employee=employeeDao.findById(id).
              orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id " + id));

      if(employeeDto.getEmail().equalsIgnoreCase(employee.getEmail())||!existsEmployeeByEmail(employeeDto.getEmail())){
          employeeMapper.updateEmployee(employee,employeeDto);
          employeeDao.save(employee);
          return employeeMapper.mapEmployeeToEmployeeDto(employee);
      }
      throw new EmployeeNotFoundException("Can't update employee with exist email");
    }

    @Transactional
    @Override
    public void deleteEmployee(long id) {
        Employee employee =employeeDao.findById(id).
                orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id " + id));
        employeeDao.delete(employee);

    }

    @Transactional
    @Override
    public EmployeeDto createEmployee(EmployeeDto employee) {
        employee.setId(0);
        if (existsEmployeeByEmail(employee.getEmail())) {
            throw new EmployeeNotFoundException("Can't create employee with exist email");
        }
        Employee newEmployee=employeeMapper.mapEmployeeDtoToEmployee(employee);
        employeeDao.save(newEmployee);
        return employeeMapper.mapEmployeeToEmployeeDto(newEmployee);
    }

    @Override
    public boolean existsEmployeeByEmail(String email) {
        return employeeDao.existsByEmail(email);
    }

    public EmployeeDto findEmployeeByEmail(String email) {
        if(existsEmployeeByEmail(email)) {
            return employeeMapper.mapEmployeeToEmployeeDto(employeeDao.findByEmail(email));
        }
        throw new EmployeeNotFoundException("Employee not found with email " + email);

    }
}

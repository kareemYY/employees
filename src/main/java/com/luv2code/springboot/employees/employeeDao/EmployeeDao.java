package com.luv2code.springboot.employees.employeeDao;

import com.luv2code.springboot.employees.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeDao extends JpaRepository<Employee,Long> {

}

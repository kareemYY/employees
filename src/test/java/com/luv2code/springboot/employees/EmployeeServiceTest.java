package com.luv2code.springboot.employees;

import com.luv2code.springboot.employees.Dto.EmployeeDto;
import com.luv2code.springboot.employees.employeeDao.EmployeeDao;
import com.luv2code.springboot.employees.entity.Employee;
import com.luv2code.springboot.employees.exception.EmployeeNotFoundException;
import com.luv2code.springboot.employees.service.EmployeeService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import java.util.List;

@TestPropertySource("/application-test.properties")
@SpringBootTest
public class EmployeeServiceTest {

    @Autowired
    private JdbcTemplate jdbc;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private EmployeeService employeeService;

    @Value("${sql.script.create.employee1}")
    private String createEmployee1;

    @Value("${sql.script.create.employee2}")
    private  String createEmployee2;

    @Value("${sql.script.create.employee3}")
    private String createEmployee3;

    @Value("${sql.script.delete.employee}")
    private String deleteEmployee;

    private long countDataBase() {
        return  entityManager.createQuery("select count(e) from Employee e " ,Long.class).getSingleResult();
    }

    @BeforeEach
    public void setup() {
        jdbc.execute(createEmployee1);
        jdbc.execute(createEmployee2);
        jdbc.execute(createEmployee3);
    }


    @Test
    public void getAllEmployeesWith(){
        List<Employee> employees = entityManager.createQuery("select e from Employee e",Employee.class).getResultList();

        List<EmployeeDto> employeeDtos= employeeService.findAll();

        assertEquals(employeeDtos.size(),countDataBase());

        assertEquals("kareem",employeeDtos.stream().findFirst().get().getFirstName());
        assertEquals(3,employeeDtos.size());
    }

    @Test
    public void getEmployeeByIdWithHappyPath(){
      Employee employeeFromEntityManager = entityManager.find(Employee.class, 1L);
      assertNotNull(employeeFromEntityManager);

      EmployeeDto employeeDtoFromService = employeeService.findEmployeeById(1);
      assertNotNull(employeeDtoFromService);

      assertEquals(employeeFromEntityManager.getId()        ,   employeeDtoFromService.getId());
      assertEquals(employeeFromEntityManager.getFirstName() ,   employeeDtoFromService.getFirstName());
      assertEquals(employeeFromEntityManager.getLastName()  ,   employeeDtoFromService.getLastName());
      assertEquals(employeeFromEntityManager.getEmail()     ,   employeeDtoFromService.getEmail());
    }

    @Test
    public void getEmployeeWithNotFoundId(){
        assertThrows(EmployeeNotFoundException.class , ()-> employeeService.findEmployeeById(10));
    }

    @Test
    public void updateEmployeeWithHappyPath(){

        assertEquals(3,countDataBase());

        EmployeeDto employeeDto= new EmployeeDto("Bros","wain","batman@wain.com");
        assertEquals(0 ,employeeDto.getId());

        EmployeeDto employeeAfterUpdate =  employeeService.updateEmployee( 3 ,  employeeDto);
        assertNotNull(employeeAfterUpdate);
        assertEquals(3,employeeAfterUpdate.getId());
        assertEquals("Bros",employeeAfterUpdate.getFirstName());
        assertEquals("wain",employeeAfterUpdate.getLastName());
        assertEquals("batman@wain.com",employeeAfterUpdate.getEmail());


        assertEquals(3,countDataBase());
    }

    @Test
    public void updateEmployeeWithNotFoundId(){
        assertEquals(3,countDataBase());

        EmployeeDto employeeDto= new EmployeeDto("Bros","wain","batman@wain.com");
        assertEquals(0 ,employeeDto.getId());

        assertThrows(EmployeeNotFoundException.class ,()-> employeeService.updateEmployee(4,employeeDto));

        assertThrows(Exception.class, ()->entityManager.
                createQuery("select e from Employee e where e.email='batman@wain.com'",Employee.class).getSingleResult());


        assertEquals(3,countDataBase());
    }

    @Test
    public void updateEmployeeEmailExist(){
        assertEquals(3,countDataBase());

        EmployeeDto employeeDto= new EmployeeDto("Bros","wain","kareem@gmail.com");
        assertEquals(0 ,employeeDto.getId());

        assertThrows(EmployeeNotFoundException.class ,()-> employeeService.updateEmployee( 3 ,  employeeDto));
    }



























    @AfterEach
    public void cleanup() {
        jdbc.execute(deleteEmployee);
    }



















}

package com.luv2code.springboot.employees;

import com.luv2code.springboot.employees.Dto.EmployeeDto;
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
    public void getAllEmployeesHappyPath(){
        assertEquals(3, countDataBase());

        List<EmployeeDto> employeeDtos= employeeService.findAll();

        assertEquals(countDataBase(),employeeDtos.size());

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

    @Test
    public void createEmployeeHappyPath(){
        assertEquals(3,countDataBase());

        EmployeeDto employeeDto = new EmployeeDto(0,"Tony" ,"Stark","ironman@stark.com");

        employeeDto= employeeService.createEmployee(employeeDto);
        assertNotNull(employeeDto);

        assertFalse(employeeDto.getId()==0);

        assertEquals(4,employeeDto.getId());
        assertEquals("Tony",employeeDto.getFirstName());
        assertEquals("Stark",employeeDto.getLastName());
        assertEquals("ironman@stark.com",employeeDto.getEmail());

        assertEquals(4,countDataBase());
    }

    @Test
    public void createEmployeeWithNoId(){
        assertEquals(3,countDataBase());

        EmployeeDto employeeDto = new EmployeeDto("Tony" ,"Stark","ironman@stark.com");
        assertEquals(0 ,employeeDto.getId());

        employeeDto= employeeService.createEmployee(employeeDto);
        assertNotNull(employeeDto);

        assertFalse(employeeDto.getId()==0);

        assertEquals(4,employeeDto.getId());
        assertEquals("Tony",employeeDto.getFirstName());
        assertEquals("Stark",employeeDto.getLastName());
        assertEquals("ironman@stark.com",employeeDto.getEmail());

        assertEquals(4,countDataBase());
    }

    @Test
    public void createEmployeeWithExistingId(){
        assertEquals(3,countDataBase());

        EmployeeDto employeeDto = new EmployeeDto(2,"Tony" ,"Stark","ironman@stark.com");
        assertEquals(2 ,employeeDto.getId());

        employeeDto= employeeService.createEmployee(employeeDto);
        assertNotNull(employeeDto);

        assertFalse(employeeDto.getId()==0);

        assertEquals(4,employeeDto.getId());
        assertEquals("Tony",employeeDto.getFirstName());
        assertEquals("Stark",employeeDto.getLastName());
        assertEquals("ironman@stark.com",employeeDto.getEmail());

        assertEquals(4,countDataBase());
    }

    @Test
    public void createEmployeeWithExistingIdAndEmail(){
        assertEquals(3,countDataBase());

        assertThrows(EmployeeNotFoundException.class,
                ()->employeeService.
                        createEmployee(new EmployeeDto(2,"Tony" ,"Stark","kareem@gmail.com")));
        assertEquals(3,countDataBase());
    }


    @Test
    public void deleteEmployeeHappyPath(){
        assertEquals(3,countDataBase());

        long id =3;
        EmployeeDto getEmployeeDto = employeeService.findEmployeeById(id);
        assertEquals("nagy",getEmployeeDto.getFirstName());
        assertEquals("galal",getEmployeeDto.getLastName());
        assertEquals("nagy@outlook.com",getEmployeeDto.getEmail());

        employeeService.deleteEmployee(3);
        assertEquals(2,countDataBase());
        assertThrows(EmployeeNotFoundException.class,()-> employeeService.findEmployeeById(id));

    }

    @Test
    public void deleteEmployeeNotFoundId(){
        assertEquals(3,countDataBase());

        assertThrows(EmployeeNotFoundException.class,()-> employeeService.deleteEmployee(5));

        assertEquals(3,countDataBase());
    }






















    @AfterEach
    public void cleanup() {
        jdbc.execute(deleteEmployee);
    }



















}

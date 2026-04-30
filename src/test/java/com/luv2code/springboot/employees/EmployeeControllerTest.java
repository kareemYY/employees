package com.luv2code.springboot.employees;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.luv2code.springboot.employees.Dto.EmployeeDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.*;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@TestPropertySource("/application-test.properties")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class EmployeeControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbc;


    private ObjectMapper objectMapper=new ObjectMapper();

    @Value("${sql.script.create.employee1}")
    private String createEmployee1;

    @Value("${sql.script.create.employee2}")
    private String createEmployee2;

    @Value("${sql.script.create.employee3}")
    private String createEmployee3;

    @Value("${sql.script.delete.employee}")
    private String deleteEmployee;



    @BeforeEach
    public void setup() {
        jdbc.execute(createEmployee1);
        jdbc.execute(createEmployee2);
        jdbc.execute(createEmployee3);
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    public void getAllEmployeesWithEmployeeRoleHttpRequest() throws Exception {
            mockMvc.perform(get("/api/employees").accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$",hasSize(3)))
                    .andExpect(jsonPath("$[0].firstName",is("kareem")))
                    .andExpect(jsonPath("$[1].email",is("amr@gmail.com")))
                    .andExpect(jsonPath("$.[2].lastName",is("galal")));
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    public void getAllEmployeesWithManagerRoleHttpRequest() throws Exception {
            mockMvc.perform(get("/api/employees").accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden());
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    public void getAllEmployeesWithAdminRoleHttpRequest() throws Exception {
        mockMvc.perform(get("/api/employees").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void getAllEmployeesWithNoRoleHttpRequest() throws Exception {
        mockMvc.perform(get("/api/employees").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    public void getOneEmployeeByIdWithEmployeeRoleHttpRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/{id}",1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName",is("kareem")))
                .andExpect(jsonPath("$.lastName",is("yasser")))
                .andExpect(jsonPath("$.email",is("kareem@gmail.com")));
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    public void getOneEmployeeByNotFoundIdWithEmployeeRoleHttpRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/{id}",100)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status",is(404)))
                .andExpect(jsonPath("$.message",is("Employee not found with id 100")));
    }

    @Test
    @WithMockUser("MANAGER")
    public void getOneEmployeeByIdWithManagerRoleHttpRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/{id}",1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser("ADMIN")
    public void getOneEmployeeByIdWithAdminRoleHttpRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/{id}",1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    public void createEmployeeWithManagerRoleHttpRequest() throws Exception {
        EmployeeDto employeeDto=new EmployeeDto(0,"steve ","jobs","jobs@icloud.com");
        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id" ,is(4)))
                .andExpect(jsonPath("$.firstName",is("steve ")))
                .andExpect(jsonPath("$.lastName",is("jobs")))
                .andExpect(jsonPath("$.email",is("jobs@icloud.com")));
    }

   @Test
   @WithMockUser(roles = "MANAGER")
   public void createEmployeeWithBlankFirstNameHttpRequest() throws Exception {
        EmployeeDto employeeDto=new EmployeeDto(0,"   ","jobs","jobs@icloud.com");
       mockMvc.perform(post("/api/employees")
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(employeeDto)))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.status",is(400)))
               .andExpect(jsonPath("$.message")
                       .value(containsString("First name must be mandatory")));
   }

    @Test
    @WithMockUser(roles = "MANAGER")
    public void createEmployeeWithOneCharacterFirstNameHttpRequest() throws Exception {
        EmployeeDto employeeDto = new EmployeeDto(0, "s", "jobs", "jobs@icloud.com");
        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message")
                        .value(containsString("FirstName must be between 2 and 50 characters")));
    }


    @Test
    @WithMockUser(roles = "MANAGER")
    public void createEmployeeWithBlankOneCharacterFirstNameHttpRequest() throws Exception {
        EmployeeDto employeeDto=new EmployeeDto(0," ","jobs","jobs@icloud.com");
        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status",is(400)))
                .andExpect(jsonPath("$.message")
                        .value(containsString("First name must be mandatory")))
                .andExpect(jsonPath("$.message")
                        .value(containsString("FirstName must be between 2 and 50 characters")));
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    public void createEmployeeWithBlankLastNameHttpRequest() throws Exception {
        EmployeeDto employeeDto=new EmployeeDto(0,"steve","  ","jobs@icloud.com");
        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status",is(400)))
                .andExpect(jsonPath("$.message")
                        .value(containsString("Last name must be mandatory")));
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    public void createEmployeeWithOneCharacterLastNameHttpRequest() throws Exception {
        EmployeeDto employeeDto = new EmployeeDto(0, "steve", "j", "jobs@icloud.com");
        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message")
                        .value(containsString("Last name must be between 2 and 50 characters")));
    }


    @Test
    @WithMockUser(roles = "MANAGER")
    public void createEmployeeWithBlankOneCharacterLastNameHttpRequest() throws Exception {
        EmployeeDto employeeDto=new EmployeeDto(0,"steve"," ","jobs@icloud.com");
        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status",is(400)))
                .andExpect(jsonPath("$.message")
                        .value(containsString("Last name must be mandatory")))
                .andExpect(jsonPath("$.message")
                        .value(containsString("Last name must be between 2 and 50 characters")));
    }


    @Test
    @WithMockUser(roles = "MANAGER")
    public void createEmployeeWithBlankEmailHttpRequest() throws Exception{
        EmployeeDto employeeDto = new EmployeeDto(0 , "steve" , "jobs" , "    ");
        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status",is(400)))
                .andExpect(jsonPath("$.message").
                        value(containsString("Email  must be mandatory")));
    }


    @Test
    @WithMockUser(roles = "MANAGER")
    public void createEmployeeWithNotValidEmailHttpRequest() throws Exception{
        EmployeeDto employeeDto = new EmployeeDto(0 , "steve" , "jobs" , "jobs_icloud");
        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status",is(400)))
                .andExpect(jsonPath("$.message").
                        value(containsString("Please provide a valid email address")));
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    public void createEmployeeWithEmployeeRoleHttpRequest() throws Exception{
        EmployeeDto employeeDto =new EmployeeDto(0,"steve ","jobs","jobs@icloud.com" );
        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void createEmployeeWithAdminRoleHttpRequest() throws Exception{
        EmployeeDto employeeDto =new EmployeeDto(0,"steve ","jobs","jobs@icloud.com" );
        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void createEmployeeWithNoRoleHttpRequest() throws Exception{
        EmployeeDto employeeDto =new EmployeeDto(0,"steve ","jobs","jobs@icloud.com" );
        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDto)))
                .andExpect(status().isUnauthorized());
    }


    @Test
    @WithMockUser(roles = "MANAGER")
    public void updateEmployeeWithManagerRoleHttpRequest()throws Exception{
        EmployeeDto employeeDto= new EmployeeDto("steve" , "jobs" ,"jobs@icloud.com");
        mockMvc.perform(put("/api/employees/{id}",2)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id",is(2)))
                .andExpect(jsonPath("$.firstName",is("steve")))
                .andExpect(jsonPath("$.lastName",is("jobs")))
                .andExpect(jsonPath("$.email",is("jobs@icloud.com")));
    }



    @Test
    @WithMockUser(roles = "MANAGER")
    public void updateEmployeeWithIdNotFoundHttpRequest() throws Exception {
        EmployeeDto employeeDto = new EmployeeDto("steve", "jobs", "jobs@icloud.com");
        mockMvc.perform(put("/api/employees/{id}", 22)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status",is(404)))
                .andExpect(jsonPath("$.message",is("Employee not found with id 22")));
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    public void updateEmployeeExistingEmailHttpRequest() throws Exception {
        EmployeeDto employeeDto = new EmployeeDto("steve", "jobs", "nagy@outlook.com");
        mockMvc.perform(put("/api/employees/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status",is(404)))
                .andExpect(jsonPath("$.message",is("Can't update employee with exist email")));
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    public void updateEmployeeCheckValidationHttpRequest() throws Exception {
        EmployeeDto employeeDto = new EmployeeDto("s", "  ", "jobs@icloud.com");
        mockMvc.perform(put("/api/employees/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status",is(400)))
                .andExpect(jsonPath("$.message")
                        .value(containsString("FirstName must be between 2 and 50 characters")))
                .andExpect(jsonPath("$.message")
                        .value(containsString("Last name must be mandatory")));
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    public void updateEmployeeWithEmployeeRoleHttpRequest() throws  Exception{
        EmployeeDto employeeDto = new EmployeeDto("steve", "jobs", "nagy@outlook.com");
        mockMvc.perform(put("/api/employees/{id}",2)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void updateEmployeeWithAdminRoleHttpRequest() throws  Exception{
        EmployeeDto employeeDto = new EmployeeDto("steve", "jobs", "nagy@outlook.com");
        mockMvc.perform(put("/api/employees/{id}",2)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void updateEmployeeWithNoRoleHttpRequest() throws  Exception{
        EmployeeDto employeeDto = new EmployeeDto("steve", "jobs", "nagy@outlook.com");
        mockMvc.perform(put("/api/employees/{id}",2)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void deleteEmployeeWithAdminRoleHttpRequest() throws Exception{
        mockMvc.perform(delete("/api/employees/{id}",3))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void deleteEmployeeAdminRoleNotFoundIdHttpRequest() throws Exception{
        mockMvc.perform(delete("/api/employees/{id}",4))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    public void deleteEmployeeWithEmployeeRoleHttpRequest() throws Exception{
        mockMvc.perform(delete("/api/employees/{id}",3))
                .andExpect(status().isForbidden());
    }
    @Test
    @WithMockUser(roles = "MANAGER")
    public void deleteEmployeeWithManagerRoleHttpRequest() throws Exception{
        mockMvc.perform(delete("/api/employees/{id}",3))
                .andExpect(status().isForbidden());
    }

    @Test
    public void deleteEmployeeNoRoleHttpRequest() throws Exception{
        mockMvc.perform(delete("/api/employees/{id}",3))
                .andExpect(status().isUnauthorized());
    }




























































    @AfterEach
    public void cleanup() {
        jdbc.execute(deleteEmployee);
    }





}
















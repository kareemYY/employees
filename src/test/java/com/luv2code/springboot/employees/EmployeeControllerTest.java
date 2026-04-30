package com.luv2code.springboot.employees;


import com.fasterxml.jackson.databind.ObjectMapper;
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
        mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/{1}",1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName",is("kareem")))
                .andExpect(jsonPath("$.lastName",is("yasser")))
                .andExpect(jsonPath("$.email",is("kareem@gmail.com")));
    }








































    @AfterEach
    public void cleanup() {
        jdbc.execute(deleteEmployee);
    }





}
















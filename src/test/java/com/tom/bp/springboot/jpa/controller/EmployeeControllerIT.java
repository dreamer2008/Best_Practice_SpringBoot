package com.tom.bp.springboot.jpa.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tom.bp.springboot.jpa.dto.EmployeeDTO;
import com.tom.bp.springboot.jpa.exception.ResourceNotFoundException;
import com.tom.bp.springboot.jpa.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class EmployeeControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeService employeeService;

    @Test
    void createEmployeeSuccessfully() throws Exception {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setFirstName("John1");
        employeeDTO.setLastName("Doe");
        employeeDTO.setEmail("john@example.com");

        when(employeeService.save(any(EmployeeDTO.class))).thenReturn(employeeDTO);

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.firstName").value("John1"))
                .andExpect(jsonPath("$.data.lastName").value("Doe"))
                .andExpect(jsonPath("$.data.email").value("john@example.com"));
    }

    @Test
    void getAllEmployeesSuccessfully() throws Exception {
        EmployeeDTO employee1 = new EmployeeDTO();
        employee1.setId(1L);
        employee1.setFirstName("John");
        employee1.setLastName("Doe");
        employee1.setEmail("john@example.com");

        EmployeeDTO employee2 = new EmployeeDTO();
        employee2.setId(2L);
        employee2.setFirstName("Jane");
        employee2.setLastName("Doe");
        employee2.setEmail("jane@example.com");

        List<EmployeeDTO> employees = Arrays.asList(employee1, employee2);
        when(employeeService.getAll()).thenReturn(employees);

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].firstName").value("John"))
                .andExpect(jsonPath("$.data[1].firstName").value("Jane"));
    }

    @Test
    void getEmployeeByIdWhenExists() throws Exception {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(1L);
        employeeDTO.setFirstName("John");
        employeeDTO.setLastName("Doe");
        employeeDTO.setEmail("john@example.com");

        when(employeeService.getById(1L)).thenReturn(employeeDTO);

        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.firstName").value("John"))
                .andExpect(jsonPath("$.data.lastName").value("Doe"))
                .andExpect(jsonPath("$.data.email").value("john@example.com"));
    }

    @Test
    void getEmployeeByIdWhenNotFound() throws Exception {
        when(employeeService.getById(999L)).thenReturn(null);

        mockMvc.perform(get("/api/employees/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    void updateEmployeeSuccessfully() throws Exception {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(1L);
        employeeDTO.setFirstName("John Updated");
        employeeDTO.setLastName("Doe");
        employeeDTO.setEmail("john@example.com");

        when(employeeService.update(anyLong(), any(EmployeeDTO.class))).thenReturn(employeeDTO);

        mockMvc.perform(put("/api/employees/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.firstName").value("John Updated"));
    }

    @Test
    void updateEmployeeNotFound() throws Exception {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(999L);

        when(employeeService.update(anyLong(), any(EmployeeDTO.class)))
                .thenThrow(new ResourceNotFoundException("Employee not found"));

        mockMvc.perform(put("/api/employees/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteEmployeeSuccessfully() throws Exception {
        doNothing().when(employeeService).deleteById(1L);

        mockMvc.perform(delete("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(204));
    }

    @Test
    void deleteEmployeeNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Employee not found"))
                .when(employeeService).deleteById(999L);

        mockMvc.perform(delete("/api/employees/999"))
                .andExpect(status().isNotFound());
    }
}

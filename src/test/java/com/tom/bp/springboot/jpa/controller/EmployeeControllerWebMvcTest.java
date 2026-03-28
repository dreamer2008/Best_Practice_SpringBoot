package com.tom.bp.springboot.jpa.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tom.bp.springboot.jpa.dao.EmployeeDao;
import com.tom.bp.springboot.jpa.dto.EmployeeDTO;
import com.tom.bp.springboot.jpa.exception.GlobalExceptionHandler;
import com.tom.bp.springboot.jpa.exception.ResourceNotFoundException;
import com.tom.bp.springboot.jpa.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
@SuppressWarnings("null")
class EmployeeControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EmployeeService employeeService;

        @MockitoBean
        private EmployeeDao employeeDao;

    @Test
    void createEmployeeShouldWrapCreatedResult() throws Exception {
        EmployeeDTO request = new EmployeeDTO(null, "Jane", "Doe", "jane.doe@example.com");
        EmployeeDTO response = new EmployeeDTO(1L, "Jane", "Doe", "jane.doe@example.com");

        when(employeeService.save(any(EmployeeDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(201))
                .andExpect(jsonPath("$.message").value("Employee is created successfully"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.email").value("jane.doe@example.com"));
    }

    @Test
    void createEmployeeShouldReturnValidationErrors() throws Exception {
        EmployeeDTO invalidRequest = new EmployeeDTO();

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message", containsString("first name could not be empty")))
                .andExpect(jsonPath("$.message", containsString("last name could not be empty")))
                .andExpect(jsonPath("$.message", containsString("email could not be empty")));
    }

    @Test
    void getAllEmployeesShouldWrapListResult() throws Exception {
        when(employeeService.getAll()).thenReturn(List.of(
                new EmployeeDTO(1L, "John", "Doe", "john.doe@example.com"),
                new EmployeeDTO(2L, "Mary", "Smith", "mary.smith@example.com")
        ));

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data[0].firstName").value("John"))
                .andExpect(jsonPath("$.data[1].lastName").value("Smith"));
    }

    @Test
    void getEmployeeByIdShouldWrapDto() throws Exception {
        when(employeeService.getById(1L)).thenReturn(new EmployeeDTO(1L, "John", "Doe", "john.doe@example.com"));

        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    void getEmployeeByIdShouldReturnNotFoundWhenServiceThrows() throws Exception {
        when(employeeService.getById(99L)).thenThrow(new ResourceNotFoundException("Employee not found with id: 99"));

        mockMvc.perform(get("/api/employees/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("Employee not found with id: 99"));
    }

    @Test
    void getEmployeesPageShouldWrapPageResult() throws Exception {
        when(employeeService.findAll(PageRequest.of(1, 2))).thenReturn(new PageImpl<>(
                List.of(new EmployeeDTO(3L, "Amy", "Wong", "amy.wong@example.com")),
                PageRequest.of(1, 2),
                1
        ));

        mockMvc.perform(get("/api/employees/page")
                        .param("pageNum", "1")
                        .param("pageSize", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.content[0].email").value("amy.wong@example.com"));
    }

    @Test
    void updateEmployeeShouldWrapUpdatedDto() throws Exception {
        EmployeeDTO request = new EmployeeDTO(null, "Updated", null, null);
        EmployeeDTO response = new EmployeeDTO(5L, "Updated", "Name", "updated.name@example.com");

        when(employeeService.update(eq(5L), any(EmployeeDTO.class))).thenReturn(response);

        mockMvc.perform(put("/api/employees/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.firstName").value("Updated"));
    }

    @Test
    void deleteEmployeeShouldReturnWrappedNoContentCode() throws Exception {
        doNothing().when(employeeService).deleteById(6L);

        mockMvc.perform(delete("/api/employees/6"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(204))
                .andExpect(jsonPath("$.message").value("Success"));
    }
}
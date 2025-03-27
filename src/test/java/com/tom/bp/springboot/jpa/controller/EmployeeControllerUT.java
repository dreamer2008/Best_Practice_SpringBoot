package com.tom.bp.springboot.jpa.controller;

import com.tom.bp.springboot.jpa.dto.EmployeeDTO;
import com.tom.bp.springboot.jpa.dto.response.base.Result;
import com.tom.bp.springboot.jpa.exception.ResourceNotFoundException;
import com.tom.bp.springboot.jpa.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class EmployeeControllerUT {

    // Test data preparation
    private final EmployeeDTO sampleDTO = new EmployeeDTO(1L, "John", "Doe", "john@example.com");

    private EmployeeService employeeService;
    private EmployeeController employeeController;


    @BeforeEach
    void setUp() {
        employeeService = mock(EmployeeService.class);
        employeeController = new EmployeeController(employeeService);
    }

    @Test
    void testGetAllEmployees_WhenNoData_ReturnEmptyList() {
        // Arrange
        when(employeeService.getAll()).thenReturn(Collections.emptyList());

        // Act
        Result<List<EmployeeDTO>> result = employeeController.getAllEmployees();

        // Assert
        assertEquals(HttpStatus.OK.value(), result.getCode());
        assertTrue(result.getData().isEmpty());
        verify(employeeService, times(1)).getAll();
    }

    @Test
    void testGetEmployeeById_WhenValidId_ReturnEmployee() {
        // Arrange
        when(employeeService.getById(1L)).thenReturn(sampleDTO);

        // Act
        Result<EmployeeDTO> result = employeeController.getEmployeeById(1L);

        // Assert
        assertEquals(HttpStatus.OK.value(), result.getCode());
        assertEquals("John", result.getData().getFirstName());
        verify(employeeService, times(1)).getById(1L);
    }

    @Test
    void testGetEmployeeById_WhenInvalidId_ThrowNotFoundException() {
        // Arrange
        when(employeeService.getById(999L)).thenThrow(new ResourceNotFoundException("Employee not found"));

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> employeeController.getEmployeeById(999L));

        assertEquals("Employee not found", exception.getMessage());
        verify(employeeService, times(1)).getById(999L);
    }

    @Test
    void testCreateEmployee_WhenValidInput_ReturnCreatedStatus() {
        // Arrange
        when(employeeService.save(any(EmployeeDTO.class))).thenReturn(sampleDTO);

        // Act
        Result<EmployeeDTO> result = employeeController.createEmployee(sampleDTO);

        // Assert
        assertEquals(HttpStatus.CREATED.value(), result.getCode());
        assertEquals("john@example.com", result.getData().getEmail());
        verify(employeeService, times(1)).save(any(EmployeeDTO.class));
    }

    @Test
    void testUpdateEmployee_WhenValidId_ReturnUpdatedData() {
        // Arrange
        when(employeeService.update(eq(1L), any(EmployeeDTO.class))).thenReturn(sampleDTO);

        // Act
        Result<EmployeeDTO> result = employeeController.updateEmployee(1L, sampleDTO);

        // Assert
        assertEquals(HttpStatus.OK.value(), result.getCode());
        assertEquals("John", result.getData().getFirstName());
        verify(employeeService, times(1)).update(eq(1L), any(EmployeeDTO.class));
    }

    @Test
    void testDeleteEmployee_WhenValidId_ReturnNoContent() {
        // Arrange
        doNothing().when(employeeService).deleteById(1L);

        // Act
        Result<Void> result = employeeController.deleteEmployee(1L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT.value(), result.getCode());
        verify(employeeService, times(1)).deleteById(1L);
    }
}


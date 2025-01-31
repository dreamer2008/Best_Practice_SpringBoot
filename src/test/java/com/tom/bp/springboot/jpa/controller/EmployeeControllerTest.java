package com.tom.bp.springboot.jpa.controller;

import com.tom.bp.springboot.jpa.dto.EmployeeDTO;
import com.tom.bp.springboot.jpa.dto.response.base.Result;
import com.tom.bp.springboot.jpa.exception.ResourceNotFoundException;
import com.tom.bp.springboot.jpa.service.EmployeeService;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class EmployeeControllerTest {

    private EmployeeService employeeService = mock(EmployeeService.class);
    private EmployeeController employeeController = new EmployeeController(employeeService);

    @Test
    void saveEmployeeWithInvalidData() {
        EmployeeDTO employeeDTO = new EmployeeDTO(); // Assuming invalid data
        when(employeeService.save(any(EmployeeDTO.class))).thenThrow(new IllegalArgumentException("Invalid data"));

        assertThrows(IllegalArgumentException.class, () -> employeeController.save(employeeDTO));
        verify(employeeService, times(1)).save(any(EmployeeDTO.class));
    }

    @Test
    void listEmployeesWhenNoneExist() {
        when(employeeService.list()).thenReturn(Collections.emptyList());

        Result<List<EmployeeDTO>> result = employeeController.list();

        assertNotNull(result);
        assertTrue(result.getData().isEmpty());
        verify(employeeService, times(1)).list();
    }

    @Test
    void getEmployeeByIdWithInvalidId() {
        when(employeeService.getById(anyLong())).thenThrow(new ResourceNotFoundException("Employee not found"));

        assertThrows(ResourceNotFoundException.class, () -> employeeController.getUserById(-1L));
        verify(employeeService, times(1)).getById(-1L);
    }

    @Test
    void updateEmployeeWithInvalidData() {
        EmployeeDTO employeeDTO = new EmployeeDTO(); // Assuming invalid data
        when(employeeService.updateUser(any(EmployeeDTO.class))).thenThrow(new IllegalArgumentException("Invalid data"));

        assertThrows(IllegalArgumentException.class, () -> employeeController.updateUser(employeeDTO));
        verify(employeeService, times(1)).updateUser(any(EmployeeDTO.class));
    }

    @Test
    void deleteEmployeeByIdWithInvalidId() {
        doThrow(new ResourceNotFoundException("Employee not found")).when(employeeService).deleteById(anyLong());

        assertThrows(ResourceNotFoundException.class, () -> employeeController.deleteUser(-1L));
        verify(employeeService, times(1)).deleteById(-1L);
    }
}

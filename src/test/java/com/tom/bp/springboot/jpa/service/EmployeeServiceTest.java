package com.tom.bp.springboot.jpa.service;

import com.tom.bp.springboot.jpa.dao.EmployeeDao;
import com.tom.bp.springboot.jpa.dto.EmployeeDTO;
import com.tom.bp.springboot.jpa.exception.ResourceNotFoundException;
import com.tom.bp.springboot.jpa.model.Employee;
import com.tom.bp.springboot.jpa.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeDao employeeDao;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Test
    void saveEmployeeSuccessfully() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        Employee employee = new Employee();
        when(employeeDao.save(any(Employee.class))).thenReturn(employee);

        EmployeeDTO result = employeeService.save(employeeDTO);

        assertNotNull(result);
        verify(employeeDao, times(1)).save(any(Employee.class));
    }

    @Test
    void listAllEmployees() {
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());
        when(employeeDao.findAll()).thenReturn(employees);

        List<EmployeeDTO> result = employeeService.list();

        assertEquals(2, result.size());
        verify(employeeDao, times(1)).findAll();
    }

    @Test
    void getEmployeeByIdSuccessfully() {
        Employee employee = new Employee();
        when(employeeDao.findById(anyLong())).thenReturn(Optional.of(employee));

        EmployeeDTO result = employeeService.getById(1L);

        assertNotNull(result);
        verify(employeeDao, times(1)).findById(1L);
    }

    @Test
    void getEmployeeByIdNotFound() {
        when(employeeDao.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> employeeService.getById(1L));
        verify(employeeDao, times(1)).findById(1L);
    }

    @Test
    void updateEmployeeSuccessfully() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        Employee employee = new Employee();
        when(employeeDao.save(any(Employee.class))).thenReturn(employee);

        EmployeeDTO result = employeeService.updateUser(employeeDTO);

        assertNotNull(result);
        verify(employeeDao, times(1)).save(any(Employee.class));
    }

    @Test
    void deleteEmployeeByIdSuccessfully() {
        doNothing().when(employeeDao).deleteById(anyLong());

        employeeService.deleteById(1L);

        verify(employeeDao, times(1)).deleteById(1L);
    }
}
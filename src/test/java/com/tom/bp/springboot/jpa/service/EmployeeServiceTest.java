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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeDao employeeDao;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Test
    void testSave() {
        Employee employee = new Employee()
                .setFirstName("John")
                .setLastName("Doe")
                .setEmail("john@example.com");
        when(employeeDao.save(any(Employee.class))).thenReturn(employee);

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setFirstName("John");
        employeeDTO.setLastName("Doe");
        employeeDTO.setEmail("john@example.com");

        EmployeeDTO savedDTO = employeeService.save(employeeDTO);
        assertNotNull(savedDTO);
        assertEquals("John", savedDTO.getFirstName());
        verify(employeeDao).save(any(Employee.class));
    }

    @Test
    void testFindAll() {
        List<Employee> employees = Arrays.asList(
                new Employee().setId(1L).setFirstName("John"),
                new Employee().setId(2L).setFirstName("Jane")
        );
        Page<Employee> employeePage = new PageImpl<>(employees);
        when(employeeDao.findAll(any(PageRequest.class))).thenReturn(employeePage);

        Page<EmployeeDTO> result = employeeService.findAll(PageRequest.of(0, 10));
        assertEquals(2, result.getContent().size());
        verify(employeeDao).findAll(any(PageRequest.class));
    }

    @Test
    void testGetAll() {
        List<Employee> employees = Arrays.asList(
                new Employee().setId(1L).setFirstName("John"),
                new Employee().setId(2L).setFirstName("Jane")
        );
        when(employeeDao.findAll()).thenReturn(employees);

        List<EmployeeDTO> result = employeeService.getAll();
        assertEquals(2, result.size());
        verify(employeeDao).findAll();
    }

    @Test
    void testGetPage() {
        List<Employee> employees = Collections.singletonList(
                new Employee().setId(1L).setFirstName("John")
        );
        Page<Employee> employeePage = new PageImpl<>(employees);
        when(employeeDao.findAll(any(PageRequest.class))).thenReturn(employeePage);

        Page<EmployeeDTO> result = employeeService.getPage(0, 10);
        assertEquals(1, result.getContent().size());
        verify(employeeDao).findAll(any(PageRequest.class));
    }

    @Test
    void testGetById() {
        Employee employee = new Employee()
                .setId(1L)
                .setFirstName("John");
        when(employeeDao.findById(1L)).thenReturn(Optional.of(employee));

        EmployeeDTO result = employeeService.getById(1L);
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        verify(employeeDao).findById(1L);
    }

    @Test
    void testGetByIdNotFound() {
        when(employeeDao.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> employeeService.getById(1L));
        verify(employeeDao).findById(1L);
    }

    @Test
    void testUpdate() {
        Employee existingEmployee = new Employee()
                .setId(1L)
                .setFirstName("John");
        when(employeeDao.findById(1L)).thenReturn(Optional.of(existingEmployee));
        when(employeeDao.save(any(Employee.class))).thenReturn(existingEmployee);

        EmployeeDTO updateDTO = new EmployeeDTO();
        updateDTO.setFirstName("Jane");

        EmployeeDTO result = employeeService.update(1L, updateDTO);
        assertNotNull(result);
        verify(employeeDao).findById(1L);
        verify(employeeDao).save(any(Employee.class));
    }

    @Test
    void testUpdateNotFound() {
        when(employeeDao.findById(1L)).thenReturn(Optional.empty());

        EmployeeDTO updateDTO = new EmployeeDTO();
        updateDTO.setFirstName("Jane");

        assertThrows(ResourceNotFoundException.class, () -> employeeService.update(1L, updateDTO));
        verify(employeeDao).findById(1L);
    }

//    @Test
//    void testDeleteById() {
//        when(employeeDao.existsById(1L)).thenReturn(true);
//        doNothing().when(employeeDao).deleteById(1L);
//
//        employeeService.deleteById(1L);
//        verify(employeeDao).existsById(1L);
//        verify(employeeDao).deleteById(1L);
//    }

    @Test
    void testDeleteByIdNotFound() {
//        when(employeeDao.existsById(1L)).thenReturn(false);
        when(employeeDao.findById(1L)).thenThrow(new ResourceNotFoundException("Employee not found with id: 1"));

        assertThrows(ResourceNotFoundException.class, () -> employeeService.deleteById(1L));
        verify(employeeDao).findById(1L);
        verify(employeeDao, never()).deleteById(anyLong());
    }
}
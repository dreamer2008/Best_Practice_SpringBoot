package com.tom.bp.springboot.jpa.mapper;

import com.tom.bp.springboot.jpa.dto.EmployeeDTO;
import com.tom.bp.springboot.jpa.model.Employee;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MapperTest {

    private final Mapper mapper = Mapper.INSTANCE;

    @Test
    void shouldMapDTOToEntityCorrectly() {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(1L);
        dto.setFirstName("Bob");
        dto.setLastName("Johnson");
        dto.setEmail("bob@test.com");

        Employee employee = mapper.toModel(dto);

        assertEquals(1L, employee.getId());
        assertEquals("Bob", employee.getFirstName());
        assertEquals("Johnson", employee.getLastName());
        assertEquals("bob@test.com", employee.getEmail());
    }

    @Test
    void shouldMapEntityToDTOCorrectly() {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("Bob");
        employee.setLastName("Johnson");
        employee.setEmail("bob@test.com");

        EmployeeDTO dto = mapper.toDTO(employee);

        assertEquals(1L, dto.getId());
        assertEquals("Bob", dto.getFirstName());
        assertEquals("Johnson", dto.getLastName());
        assertEquals("bob@test.com", dto.getEmail());
    }

    @Test
    void shouldMapDTOListToEntityListCorrectly() {
        EmployeeDTO dto1 = new EmployeeDTO();
        dto1.setId(1L);
        dto1.setFirstName("Carol");

        EmployeeDTO dto2 = new EmployeeDTO();
        dto2.setId(2L);
        dto2.setFirstName("Dave");

        List<EmployeeDTO> dtos = Arrays.asList(dto1, dto2);
        List<Employee> employees = mapper.toModels(dtos);

        assertEquals(2, employees.size());
        assertEquals(1L, employees.get(0).getId());
        assertEquals("Carol", employees.get(0).getFirstName());
        assertEquals(2L, employees.get(1).getId());
        assertEquals("Dave", employees.get(1).getFirstName());
    }

    @Test
    void shouldMapEntityListToDTOListCorrectly() {
        Employee emp1 = new Employee();
        emp1.setId(1L);
        emp1.setFirstName("Eve");

        Employee emp2 = new Employee();
        emp2.setId(2L);
        emp2.setFirstName("Frank");

        List<Employee> employees = Arrays.asList(emp1, emp2);
        List<EmployeeDTO> dtos = mapper.toDTOs(employees);

        assertEquals(2, dtos.size());
        assertEquals(1L, dtos.get(0).getId());
        assertEquals("Eve", dtos.get(0).getFirstName());
        assertEquals(2L, dtos.get(1).getId());
        assertEquals("Frank", dtos.get(1).getFirstName());
    }
}
package com.tom.bp.springboot.jpa.service;

import com.tom.bp.springboot.jpa.dto.EmployeeDTO;

import java.util.List;

public interface EmployeeService {

    EmployeeDTO save(EmployeeDTO employeeDTO);

    List<EmployeeDTO> list();

    EmployeeDTO getById(Long id);

    EmployeeDTO updateUser(EmployeeDTO employeeDTO);

    void deleteById(Long id);
}

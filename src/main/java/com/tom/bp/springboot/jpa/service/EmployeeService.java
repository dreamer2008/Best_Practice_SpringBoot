package com.tom.bp.springboot.jpa.service;

import com.tom.bp.springboot.jpa.dto.EmployeeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {

    EmployeeDTO save(EmployeeDTO employeeDTO);

    Page<EmployeeDTO> findAll(Pageable pageable);

    List<EmployeeDTO> getAll();

    Page<EmployeeDTO> getPage(int pageNum, int pageSize);

    EmployeeDTO getById(Long id);

    EmployeeDTO update(Long id, EmployeeDTO employeeDTO);

    void deleteById(Long id);
}

package com.tom.bp.springboot.jpa.service.impl;

import com.tom.bp.springboot.jpa.dao.EmployeeDao;
import com.tom.bp.springboot.jpa.dto.EmployeeDTO;
import com.tom.bp.springboot.jpa.exception.ResourceNotFoundException;
import com.tom.bp.springboot.jpa.model.Employee;
import com.tom.bp.springboot.jpa.service.EmployeeService;
import com.tom.bp.springboot.jpa.util.Mapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Resource
    private EmployeeDao employeeDao;

    public EmployeeDTO save(EmployeeDTO employeeDTO) {
        Employee employee = Mapper.INSTANCE.toModel(employeeDTO);
        Date now = new Date();
        employee.setCreatedAt(now).setUpdateAt(now);
        return Mapper.INSTANCE.toDTO(employeeDao.save(employee));
    }

    public List<EmployeeDTO> list() {
        List<Employee> list = employeeDao.findAll();
        return Mapper.INSTANCE.toDTOs(list);
    }

    public EmployeeDTO getById(Long id) {
        return Mapper.INSTANCE.toDTO(employeeDao.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee not found")));
    }

    public EmployeeDTO updateUser(EmployeeDTO employeeDTO) {
        Employee employee = Mapper.INSTANCE.toModel(employeeDTO);
        Date now = new Date();
        employee.setCreatedAt(now).setUpdateAt(now);
        return Mapper.INSTANCE.toDTO(employeeDao.save(employee));
    }

    public void deleteById(Long id) {
        employeeDao.deleteById(id);
    }
}

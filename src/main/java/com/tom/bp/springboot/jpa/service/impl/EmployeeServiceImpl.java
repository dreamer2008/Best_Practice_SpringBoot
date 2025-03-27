package com.tom.bp.springboot.jpa.service.impl;

import com.tom.bp.springboot.jpa.dao.EmployeeDao;
import com.tom.bp.springboot.jpa.dto.EmployeeDTO;
import com.tom.bp.springboot.jpa.exception.ResourceNotFoundException;
import com.tom.bp.springboot.jpa.model.Employee;
import com.tom.bp.springboot.jpa.service.EmployeeService;
import com.tom.bp.springboot.jpa.util.JpaUtil;
import com.tom.bp.springboot.jpa.mapper.Mapper;
import com.tom.bp.springboot.jpa.util.enums.EnumState;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeDao employeeDao;

    private final Mapper employeeMapper = Mapper.INSTANCE;


    @Transactional
    public EmployeeDTO save(EmployeeDTO employeeDTO) {
        Employee employee = employeeMapper.toModel(employeeDTO);
        Date now = new Date();
        employee.setCreatedAt(now).setUpdateAt(now).setState(EnumState.VALID.ordinal());
        return employeeMapper.toDTO(employeeDao.save(employee));
    }

    @Override
    public Page<EmployeeDTO> findAll(Pageable pageable) {
        Page<Employee> all = employeeDao.findAll(pageable);
        return all.map(employeeMapper::toDTO);

    }

    @Transactional(readOnly = true)
    public List<EmployeeDTO> getAll() {
        List<Employee> list = employeeDao.findAll();
        return employeeMapper.toDTOs(list);
    }

    @Transactional(readOnly = true)
    public Page<EmployeeDTO> getPage(int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        return employeeDao.findAll(pageable).map(employeeMapper::toDTO);
    }


    @Transactional(readOnly = true)
    public EmployeeDTO getById(Long id) {
//        Employee employee = employeeDao.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        Employee employee = findEmployeeOrThrow(id);
        return employeeMapper.toDTO(employee);
    }

    @Transactional
    public EmployeeDTO update(Long id, EmployeeDTO employeeDTO) {
        Employee employeeFound = findEmployeeOrThrow(id);
        JpaUtil.copyNotNullProperties(employeeDTO, employeeFound);
        Date now = new Date();
        employeeFound.setCreatedAt(now).setUpdateAt(now);
        Employee updated = employeeDao.save(employeeFound);
        return employeeMapper.toDTO(updated);
    }

    @Transactional
    public void deleteById(Long id) {
        findEmployeeOrThrow(id);
        employeeDao.deleteById(id);
    }

    private Employee findEmployeeOrThrow(Long id) {
        return employeeDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
    }
}

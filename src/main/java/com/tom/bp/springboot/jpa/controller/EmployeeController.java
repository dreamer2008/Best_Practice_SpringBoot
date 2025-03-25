package com.tom.bp.springboot.jpa.controller;

import com.tom.bp.springboot.jpa.dto.EmployeeDTO;
import com.tom.bp.springboot.jpa.dto.response.base.Result;
import com.tom.bp.springboot.jpa.service.EmployeeService;
import com.tom.bp.springboot.jpa.validate.EmployeeAddGroup;
import com.tom.bp.springboot.jpa.validate.EmployeeUpdateGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/employees")
@Slf4j
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public Result<EmployeeDTO> createEmployee(@Validated({EmployeeAddGroup.class}) @RequestBody EmployeeDTO employeeDTO) {
        log.info("employee creation param: {}", employeeDTO);
        if (employeeDTO == null
                || !StringUtils.hasText(employeeDTO.getFirstName())
                || !StringUtils.hasText(employeeDTO.getLastName())
                || !StringUtils.hasText(employeeDTO.getEmail())) {
            return Result.fail(HttpStatus.BAD_REQUEST.value(), "Invalid request", null);
        }
//        int i = 1 / 0;
        EmployeeDTO savedEmployee = employeeService.save(employeeDTO);
        return Result.success(HttpStatus.CREATED.value(), "Success", savedEmployee);
    }

    @GetMapping
    public Result<List<EmployeeDTO>> getAllEmployees() {
        return Result.success(employeeService.list());
    }

    @GetMapping("/{id}")
    public Result<EmployeeDTO> getEmployee(@PathVariable Long id) {
        EmployeeDTO employeeDTO = employeeService.getById(id);
        if (employeeDTO == null) {
            return Result.success(HttpStatus.NOT_FOUND.value(), "Employee with id" + id + " not found", null);
        }
        return Result.success(employeeDTO);
    }

    @PutMapping
    public Result updateEmployee(@Validated({EmployeeUpdateGroup.class}) @RequestBody EmployeeDTO employeeDTO) {
        return Result.success(employeeService.updateUser(employeeDTO));
    }

    @DeleteMapping("/{id}")
    public Result deleteEmployee(@PathVariable Long id) {
        employeeService.deleteById(id);
        return Result.success();
    }

}

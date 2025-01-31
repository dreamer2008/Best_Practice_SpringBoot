package com.tom.bp.springboot.jpa.controller;

import com.tom.bp.springboot.jpa.dto.EmployeeDTO;
import com.tom.bp.springboot.jpa.dto.response.base.Result;
import com.tom.bp.springboot.jpa.service.EmployeeService;
import com.tom.bp.springboot.jpa.validate.EmployeeAddGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Result<EmployeeDTO> save(@Validated(EmployeeAddGroup.class) @RequestBody EmployeeDTO employeeDTO) {
        log.info("employee creation param: " + employeeDTO);
        EmployeeDTO savedEmployee = employeeService.save(employeeDTO);
        return Result.success(savedEmployee);
    }

    @GetMapping
    public Result<List<EmployeeDTO>> list() {
        return Result.success(employeeService.list());
    }

    @GetMapping("/{id}")
    public Result<EmployeeDTO> getUserById(@PathVariable Long id) {
        EmployeeDTO employeeDTO = employeeService.getById(id);
        return Result.success(employeeDTO);
    }

    @PutMapping
    public Result updateUser(@Validated @RequestBody EmployeeDTO employeeDTO) {
        return Result.success(employeeService.updateUser(employeeDTO));
    }

    @DeleteMapping("/{id}")
    public Result deleteUser(@PathVariable Long id) {
        employeeService.deleteById(id);
        return Result.success();
    }

}

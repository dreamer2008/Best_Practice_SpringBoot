package com.tom.bp.springboot.jpa.controller;

import com.tom.bp.springboot.jpa.dto.EmployeeDTO;
import com.tom.bp.springboot.jpa.dto.response.base.Result;
import com.tom.bp.springboot.jpa.service.EmployeeService;
import com.tom.bp.springboot.jpa.validate.EmployeeAddGroup;
import com.tom.bp.springboot.jpa.validate.EmployeeUpdateGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
        EmployeeDTO savedEmployee = employeeService.save(employeeDTO);
        return Result.success(HttpStatus.CREATED.value(), "Employee is created successfully", savedEmployee);
    }

    @GetMapping
    public Result<List<EmployeeDTO>> getAllEmployees() {
        return Result.success(employeeService.getAll());
    }

    @GetMapping("/{id}")
    public Result<EmployeeDTO> getEmployeeById(@PathVariable Long id) {
        EmployeeDTO employeeDTO = employeeService.getById(id);
        if (employeeDTO == null) {
            return Result.success(HttpStatus.NOT_FOUND.value(), "Employee with id" + id + " not found", null);
        }
        return Result.success(employeeDTO);
    }

    @GetMapping("/page")
    public Result<Page<EmployeeDTO>> getAllEmployees(
            @RequestParam(defaultValue = "0") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<EmployeeDTO> employeeDTOPage = employeeService.findAll(PageRequest.of(pageNum, pageSize));
        if (employeeDTOPage.isEmpty()) {
            return Result.fail(HttpStatus.NOT_FOUND.value(), "No employees found", null);
        }
        return Result.success(employeeDTOPage);
    }

    @PutMapping("/{id}")
    public Result<EmployeeDTO> updateEmployee(@PathVariable Long id, @Validated({EmployeeUpdateGroup.class}) @RequestBody EmployeeDTO employeeDTO) {
        return Result.success(employeeService.update(id, employeeDTO));
    }

    @DeleteMapping("/{id}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Result<Void> deleteEmployee(@PathVariable Long id) {
//        int i = 1 / 0;
        employeeService.deleteById(id);
        return Result.fail(HttpStatus.NO_CONTENT.value(), "Success", null);
    }

}

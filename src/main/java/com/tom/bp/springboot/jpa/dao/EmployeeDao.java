package com.tom.bp.springboot.jpa.dao;

import com.tom.bp.springboot.jpa.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeDao extends JpaRepository<Employee, Long> {

//    Page<Employee> findAll(String lastName, Pageable pageable);
}

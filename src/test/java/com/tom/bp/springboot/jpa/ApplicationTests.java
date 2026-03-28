package com.tom.bp.springboot.jpa;

import com.tom.bp.springboot.jpa.controller.EmployeeController;
import com.tom.bp.springboot.jpa.dao.EmployeeDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ApplicationTests {

    @Autowired
    private EmployeeController employeeController;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private EmployeeDao employeeDao;

    @Test
    void contextLoads() {
        assertNotNull(employeeController);
        assertNotNull(applicationContext);
        assertTrue(employeeDao.count() >= 2);
    }

}

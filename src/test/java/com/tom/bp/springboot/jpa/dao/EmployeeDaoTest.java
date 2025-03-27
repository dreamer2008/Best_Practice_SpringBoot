package com.tom.bp.springboot.jpa.dao;

import com.tom.bp.springboot.jpa.model.Employee;
import com.tom.bp.springboot.jpa.util.enums.EnumState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EmployeeDaoTest {

    @Autowired
    private EmployeeDao employeeDao;

    @Test
    void testSaveAndRetrieveEmployee() {
        Date now = new Date();
        Employee employee = new Employee(null, "Mark", "Smith", "mark.smith@example.com", EnumState.VALID.ordinal(), now, now);
        employee = employeeDao.save(employee);
        Optional<Employee> foundEmployee = employeeDao.findById(employee.getId());
        assertThat(foundEmployee).isPresent();
        assertThat(foundEmployee.get().getFirstName()).isEqualTo("Mark");
        assertThat(foundEmployee.get().getLastName()).isEqualTo("Smith");
    }
}

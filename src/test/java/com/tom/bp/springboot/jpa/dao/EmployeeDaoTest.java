package com.tom.bp.springboot.jpa.dao;

import com.tom.bp.springboot.jpa.model.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@SuppressWarnings("null")
class EmployeeDaoTest {

    @Autowired
    private EmployeeDao employeeDao;

    @Test
    void saveShouldPersistAndFindEmployee() {
        Employee employee = employee("Alice", "Walker", "alice.walker@example.com");

        Employee persisted = employeeDao.save(employee);

        assertThat(persisted.getId()).isNotNull();
        assertThat(employeeDao.findById(persisted.getId()))
                .get()
                .extracting(Employee::getEmail, Employee::getFirstName)
                .containsExactly("alice.walker@example.com", "Alice");
    }

    @Test
    void findAllShouldSupportPaging() {
        employeeDao.save(employee("Tom", "A", "tom.a@example.com"));
        employeeDao.save(employee("Tom", "B", "tom.b@example.com"));

        Page<Employee> page = employeeDao.findAll(PageRequest.of(0, 10));

        assertThat(page.getTotalElements()).isGreaterThanOrEqualTo(2);
        assertThat(page.getContent())
                .extracting(Employee::getEmail)
                .contains("tom.a@example.com", "tom.b@example.com");
    }

    private static Employee employee(String firstName, String lastName, String email) {
        Date now = new Date();
        return new Employee()
                .setFirstName(firstName)
                .setLastName(lastName)
                .setEmail(email)
                .setState(1)
                .setCreatedAt(now)
                .setUpdateAt(now);
    }
}

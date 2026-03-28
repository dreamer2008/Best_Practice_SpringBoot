package com.tom.bp.springboot.jpa.util;

import com.tom.bp.springboot.jpa.dto.EmployeeDTO;
import com.tom.bp.springboot.jpa.model.Employee;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JpaUtilTest {

    @Test
    void copyNotNullPropertiesShouldPreserveExistingValuesForNullFields() {
        EmployeeDTO source = new EmployeeDTO().setFirstName("Updated");
        Employee target = new Employee()
                .setFirstName("Old")
                .setLastName("Name")
                .setEmail("old.name@example.com");

        JpaUtil.copyNotNullProperties(source, target);

        assertThat(target.getFirstName()).isEqualTo("Updated");
        assertThat(target.getLastName()).isEqualTo("Name");
        assertThat(target.getEmail()).isEqualTo("old.name@example.com");
    }
}
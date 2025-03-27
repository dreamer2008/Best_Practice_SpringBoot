package com.tom.bp.springboot.jpa;

import com.tom.bp.springboot.jpa.dao.EmployeeDao;
import com.tom.bp.springboot.jpa.model.Employee;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Date;

@SpringBootApplication(exclude = {
        SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class})
//@EnableWebSecurity
@EnableTransactionManagement
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    @Bean
    CommandLineRunner init(EmployeeDao employeeDao) {
        return args -> {
            Date now = new Date();
            Employee employee1 = new Employee().setFirstName("John").setLastName("Doe").setEmail("john.doe@example.com").setCreatedAt(now).setUpdateAt(now);
            Employee employee2 = new Employee().setFirstName("Mary").setLastName("Smith").setEmail("mary.smith@example.com").setCreatedAt(now).setUpdateAt(now);
            employeeDao.save(employee1);
            employeeDao.save(employee2);
        };
    }

}

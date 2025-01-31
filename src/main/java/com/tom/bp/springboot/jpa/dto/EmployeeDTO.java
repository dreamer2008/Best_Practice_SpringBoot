package com.tom.bp.springboot.jpa.dto;

import com.tom.bp.springboot.jpa.validate.EmployeeAddGroup;
import com.tom.bp.springboot.jpa.validate.EmployeeUpdateGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class EmployeeDTO {

    @NotNull(message = "employee id could not be empty", groups = {EmployeeUpdateGroup.class})
    private Long id;

    @NotBlank(message = "first name could not be empty", groups = {EmployeeAddGroup.class})
    private String firstName;

    @NotBlank(message = "last name could not be empty", groups = {EmployeeAddGroup.class})
    private String lastName;

    @NotBlank(message = "email could not be empty", groups = {EmployeeAddGroup.class})
    private String email;

}

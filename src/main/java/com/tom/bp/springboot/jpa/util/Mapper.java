package com.tom.bp.springboot.jpa.util;

import com.tom.bp.springboot.jpa.dto.EmployeeDTO;
import com.tom.bp.springboot.jpa.model.Employee;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@org.mapstruct.Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface Mapper {

    Mapper INSTANCE = Mappers.getMapper(Mapper.class);

    Employee toModel(EmployeeDTO employeeDTO);

    List<Employee> toModels(List<EmployeeDTO> list);
    
    EmployeeDTO toDTO(Employee employee);

    List<EmployeeDTO> toDTOs(List<Employee> list);

}

package com.tom.bp.springboot.jpa.mapper;

import com.tom.bp.springboot.jpa.dto.EmployeeDTO;
import com.tom.bp.springboot.jpa.model.Employee;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmployeeMapper {

    Employee toModel(EmployeeDTO employeeDTO);

    List<Employee> toModels(List<EmployeeDTO> list);

    EmployeeDTO toDTO(Employee employee);

    List<EmployeeDTO> toDTOs(List<Employee> list);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(EmployeeDTO dto, @MappingTarget Employee entity);
}

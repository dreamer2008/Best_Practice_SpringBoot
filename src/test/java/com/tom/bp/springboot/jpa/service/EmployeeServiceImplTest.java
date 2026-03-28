package com.tom.bp.springboot.jpa.service;

import com.tom.bp.springboot.jpa.dao.EmployeeDao;
import com.tom.bp.springboot.jpa.dto.EmployeeDTO;
import com.tom.bp.springboot.jpa.exception.ResourceNotFoundException;
import com.tom.bp.springboot.jpa.mapper.EmployeeMapper;
import com.tom.bp.springboot.jpa.model.Employee;
import com.tom.bp.springboot.jpa.service.impl.EmployeeServiceImpl;
import com.tom.bp.springboot.jpa.util.enums.EnumState;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
class EmployeeServiceImplTest {

    @Mock
    private EmployeeDao employeeDao;

    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Test
    void saveShouldSetAuditFieldsAndReturnMappedDto() {
        EmployeeDTO request = employeeDto(null, "Jane", "Doe", "jane.doe@example.com");
        Employee mapped = employee(null, "Jane", "Doe", "jane.doe@example.com");
        Employee persisted = employee(10L, "Jane", "Doe", "jane.doe@example.com");
        EmployeeDTO response = employeeDto(10L, "Jane", "Doe", "jane.doe@example.com");

        when(employeeMapper.toModel(request)).thenReturn(mapped);
        when(employeeDao.save(mapped)).thenReturn(persisted);
        when(employeeMapper.toDTO(persisted)).thenReturn(response);

        EmployeeDTO saved = employeeService.save(request);

        assertThat(saved).isSameAs(response);
        assertThat(mapped.getState()).isEqualTo(EnumState.VALID.ordinal());
        assertThat(mapped.getCreatedAt()).isNotNull();
        assertThat(mapped.getUpdateAt()).isNotNull();
        verify(employeeMapper).toModel(request);
        verify(employeeDao).save(mapped);
        verify(employeeMapper).toDTO(persisted);
    }

    @Test
    void findAllShouldMapRepositoryPage() {
        PageRequest pageRequest = PageRequest.of(0, 5);
        Employee entity = employee(1L, "John", "Doe", "john.doe@example.com");
        EmployeeDTO dto = employeeDto(1L, "John", "Doe", "john.doe@example.com");
        Page<Employee> page = new PageImpl<>(List.of(entity), pageRequest, 1);

        when(employeeDao.findAll(pageRequest)).thenReturn(page);
        when(employeeMapper.toDTO(entity)).thenReturn(dto);

        Page<EmployeeDTO> result = employeeService.findAll(pageRequest);

        assertThat(result.getContent()).containsExactly(dto);
        verify(employeeDao).findAll(pageRequest);
    }

    @Test
    void getAllShouldReturnMappedDtos() {
        List<Employee> entities = List.of(
                employee(1L, "John", "Doe", "john.doe@example.com"),
                employee(2L, "Mary", "Smith", "mary.smith@example.com")
        );
        List<EmployeeDTO> dtos = List.of(
                employeeDto(1L, "John", "Doe", "john.doe@example.com"),
                employeeDto(2L, "Mary", "Smith", "mary.smith@example.com")
        );

        when(employeeDao.findAll()).thenReturn(entities);
        when(employeeMapper.toDTOs(entities)).thenReturn(dtos);

        assertThat(employeeService.getAll()).containsExactlyElementsOf(dtos);
    }

    @Test
    void getPageShouldUsePageRequestAndMapDtos() {
        Employee entity = employee(3L, "Amy", "Wong", "amy.wong@example.com");
        EmployeeDTO dto = employeeDto(3L, "Amy", "Wong", "amy.wong@example.com");
        PageRequest pageRequest = PageRequest.of(1, 2);
        Page<Employee> page = new PageImpl<>(List.of(entity), pageRequest, 1);

        when(employeeDao.findAll(pageRequest)).thenReturn(page);
        when(employeeMapper.toDTO(entity)).thenReturn(dto);

        Page<EmployeeDTO> result = employeeService.getPage(1, 2);

        assertThat(result.getContent()).containsExactly(dto);
        verify(employeeDao).findAll(pageRequest);
    }

    @Test
    void getByIdShouldReturnMappedDto() {
        Employee entity = employee(4L, "Luke", "Sky", "luke.sky@example.com");
        EmployeeDTO dto = employeeDto(4L, "Luke", "Sky", "luke.sky@example.com");

        when(employeeDao.findById(4L)).thenReturn(Optional.of(entity));
        when(employeeMapper.toDTO(entity)).thenReturn(dto);

        assertThat(employeeService.getById(4L)).isSameAs(dto);
    }

    @Test
    void getByIdShouldThrowWhenEmployeeMissing() {
        when(employeeDao.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.getById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id: 99");
    }

    @Test
    void updateShouldCopyOnlyNonNullPropertiesAndPersist() {
        EmployeeDTO updateRequest = new EmployeeDTO().setFirstName("Updated");
        Employee existing = employee(5L, "Old", "Name", "old.name@example.com");
        Employee persisted = employee(5L, "Updated", "Name", "old.name@example.com");
        EmployeeDTO response = employeeDto(5L, "Updated", "Name", "old.name@example.com");

        when(employeeDao.findById(5L)).thenReturn(Optional.of(existing));
        when(employeeDao.save(existing)).thenReturn(persisted);
        when(employeeMapper.toDTO(persisted)).thenReturn(response);

        EmployeeDTO updated = employeeService.update(5L, updateRequest);

        assertThat(updated).isSameAs(response);
        ArgumentCaptor<Employee> employeeCaptor = ArgumentCaptor.forClass(Employee.class);
        verify(employeeDao).save(employeeCaptor.capture());
        Employee saved = employeeCaptor.getValue();
        assertThat(saved.getFirstName()).isEqualTo("Updated");
        assertThat(saved.getLastName()).isEqualTo("Name");
        assertThat(saved.getEmail()).isEqualTo("old.name@example.com");
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdateAt()).isNotNull();
    }

    @Test
    void updateShouldThrowWhenEmployeeMissing() {
        when(employeeDao.findById(100L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.update(100L, new EmployeeDTO()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id: 100");
        verify(employeeDao, never()).save(any());
    }

    @Test
    void deleteByIdShouldDeleteWhenEmployeeExists() {
        when(employeeDao.findById(6L)).thenReturn(Optional.of(employee(6L, "Delete", "Me", "delete.me@example.com")));

        employeeService.deleteById(6L);

        verify(employeeDao).deleteById(6L);
    }

    @Test
    void deleteByIdShouldThrowWhenEmployeeMissing() {
        when(employeeDao.findById(7L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.deleteById(7L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id: 7");
        verify(employeeDao, never()).deleteById(any());
    }

    private static EmployeeDTO employeeDto(Long id, String firstName, String lastName, String email) {
        return new EmployeeDTO(id, firstName, lastName, email);
    }

    private static Employee employee(Long id, String firstName, String lastName, String email) {
        return new Employee()
                .setId(id)
                .setFirstName(firstName)
                .setLastName(lastName)
                .setEmail(email);
    }
}
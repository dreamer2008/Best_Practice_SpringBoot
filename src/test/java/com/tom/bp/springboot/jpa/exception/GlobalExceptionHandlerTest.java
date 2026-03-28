package com.tom.bp.springboot.jpa.exception;

import com.tom.bp.springboot.jpa.dto.EmployeeDTO;
import com.tom.bp.springboot.jpa.dto.response.base.Result;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("null")
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleShouldAggregateValidationMessages() throws NoSuchMethodException {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new EmployeeDTO(), "employeeDTO");
        bindingResult.addError(new FieldError("employeeDTO", "firstName", "first name could not be empty"));
        bindingResult.addError(new FieldError("employeeDTO", "email", "email could not be empty"));
        MethodParameter parameter = new MethodParameter(
                TestController.class.getDeclaredMethod("create", EmployeeDTO.class),
                0
        );
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(parameter, bindingResult);

        Result<?> result = handler.handle(exception);

        assertThat(result.getCode()).isEqualTo(400);
        assertThat(result.getMessage()).contains("first name could not be empty");
        assertThat(result.getMessage()).contains("email could not be empty");
    }

    @Test
    void handleResourceNotFoundExceptionShouldReturnNotFoundResult() {
        Result<?> result = handler.handleResourceNotFoundException(new ResourceNotFoundException("missing employee"));

        assertThat(result.getCode()).isEqualTo(404);
        assertThat(result.getMessage()).isEqualTo("missing employee");
        assertThat(result.getData()).isNull();
    }

    @Test
    void handleExceptionShouldReturnInternalServerErrorResult() {
        Result<String> result = handler.handleException(new IllegalStateException("boom"));

        assertThat(result.getCode()).isEqualTo(500);
        assertThat(result.getMessage()).isEqualTo("boom");
    }

    static class TestController {
        void create(EmployeeDTO employeeDTO) {
        }
    }
}
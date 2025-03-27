package com.tom.bp.springboot.jpa.exception;

import com.tom.bp.springboot.jpa.dto.response.base.Result;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @Test
    void methodArgumentNotValidExceptionReturnsBadRequestWithAllErrors() {
        BindingResult bindingResult = mock(BindingResult.class);
        List<ObjectError> errors = Arrays.asList(
                new ObjectError("field1", "error1"),
                new ObjectError("field2", "error2")
        );
        when(bindingResult.getAllErrors()).thenReturn(errors);

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        Result<?> result = exceptionHandler.handle(ex);

        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getCode());
        assertEquals("error1;error2", result.getMessage());
    }

    @Test
    void resourceNotFoundExceptionReturnsNotFoundStatus() {
        String message = "Resource not found";
        ResourceNotFoundException ex = new ResourceNotFoundException(message);

        Result<?> result = exceptionHandler.handleResourceNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND.value(), result.getCode());
        assertEquals(message, result.getMessage());
    }

    @Test
    void generalExceptionReturnsInternalServerError() {
        String message = "Unexpected error";
        Exception ex = new RuntimeException(message);

        Result<String> result = exceptionHandler.handleException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), result.getCode());
        assertEquals(message, result.getMessage());
    }

    @Test
    void emptyValidationErrorReturnsEmptyMessage() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getAllErrors()).thenReturn(Collections.emptyList());

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        Result<?> result = exceptionHandler.handle(ex);

        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getCode());
        assertEquals("", result.getMessage());
    }
}
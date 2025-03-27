package com.tom.bp.springboot.jpa.dto.response.base;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class ResultTest {

    @Test
    void successReturnsResultWithOkStatusAndData() {
        String data = "test data";
        Result<String> result = Result.success(data);

        assertEquals(HttpStatus.OK.value(), result.getCode());
        assertEquals("Success", result.getMessage());
        assertEquals(data, result.getData());
    }

    @Test
    void successWithoutDataReturnsResultWithOkStatus() {
        Result<Object> result = Result.success();

        assertEquals(HttpStatus.OK.value(), result.getCode());
        assertEquals("Success", result.getMessage());
        assertNull(result.getData());
    }

    @Test
    void successWithCustomValuesReturnsResultWithProvidedValues() {
        Integer code = 201;
        String message = "Created";
        String data = "test data";

        Result<String> result = Result.success(code, message, data);

        assertEquals(code, result.getCode());
        assertEquals(message, result.getMessage());
        assertEquals(data, result.getData());
    }

    @Test
    void failReturnsResultWithProvidedErrorValues() {
        Integer code = 404;
        String message = "Not Found";

        Result<Object> result = Result.fail(code, message);

        assertEquals(code, result.getCode());
        assertEquals(message, result.getMessage());
        assertNull(result.getData());
    }

    @Test
    void failWithDataReturnsResultWithProvidedErrorValuesAndData() {
        Integer code = 400;
        String message = "Bad Request";
        String data = "error details";

        Result<String> result = Result.fail(code, message, data);

        assertEquals(code, result.getCode());
        assertEquals(message, result.getMessage());
        assertEquals(data, result.getData());
    }

    @Test
    void builderCreatesResultWithAllValues() {
        Integer code = 200;
        String message = "Test";
        String data = "test data";

        Result<String> result = Result.<String>builder()
                .code(code)
                .message(message)
                .data(data)
                .build();

        assertEquals(code, result.getCode());
        assertEquals(message, result.getMessage());
        assertEquals(data, result.getData());
    }
}
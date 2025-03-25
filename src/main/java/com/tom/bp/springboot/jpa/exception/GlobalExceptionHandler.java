package com.tom.bp.springboot.jpa.exception;

import com.tom.bp.springboot.jpa.dto.response.base.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.StringJoiner;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result<?> handle(MethodArgumentNotValidException e) {
        StringJoiner joiner = new StringJoiner(";");
        for (ObjectError error : e.getBindingResult().getAllErrors()) {
            joiner.add(error.getDefaultMessage());
        }
        log.error("Param validation failed: {}", joiner.toString());
        return handleException(joiner.toString());
    }

    private Result<?> handleException(String msg) {
        Result<?> result = new Result<>();
        result.setMessage(msg);
        result.setCode(500);
        return result;
    }
}
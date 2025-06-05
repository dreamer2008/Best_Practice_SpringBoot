package com.tom.bp.springboot.jpa.exception;

import com.tom.bp.springboot.jpa.dto.response.base.Result;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.StringJoiner;

@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handle(MethodArgumentNotValidException e) {
        StringJoiner joiner = new StringJoiner(";");
        for (ObjectError error : e.getBindingResult().getAllErrors()) {
            joiner.add(error.getDefaultMessage());
        }
        log.error("Param validation failed: {}", joiner.toString());
        return handleException(joiner.toString());
    }

    private Result<?> handleException(String msg) {
        return Result.fail(HttpStatus.BAD_REQUEST.value(), msg);
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Result handleResourceNotFoundException(ResourceNotFoundException ex) {
        return Result.fail(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception ex) {
        return Result.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
    }

}
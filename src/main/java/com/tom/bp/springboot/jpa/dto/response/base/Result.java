package com.tom.bp.springboot.jpa.dto.response.base;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

/**
 * @author Tom
 *
 */
@Getter
@Setter
@Builder
@ToString
public class Result<T> {

  private Integer code;
  private String message;

  private T data;

  public Result() {
  }

  public Result(Integer code, String message, T data) {
    this.code = code;
    this.message = message;
    this.data = data;
  }

  public static <T> Result<T> success(T data){
    return Result.<T>builder()
        .code(HttpStatus.OK.value())
        .message("Success")
        .data(data)
        .build();
  }

  public static <T> Result<T> success(){
    return Result.<T>builder()
            .code(HttpStatus.OK.value())
            .message("Success")
            .build();
  }

}

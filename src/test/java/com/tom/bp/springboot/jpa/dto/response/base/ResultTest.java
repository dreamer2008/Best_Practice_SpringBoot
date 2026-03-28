package com.tom.bp.springboot.jpa.dto.response.base;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ResultTest {

    @Test
    void successShouldUseDefaultOkMetadata() {
        Result<String> result = Result.success("payload");

        assertThat(result.getCode()).isEqualTo(200);
        assertThat(result.getMessage()).isEqualTo("Success");
        assertThat(result.getData()).isEqualTo("payload");
    }

    @Test
    void successWithoutDataShouldReturnEmptyPayload() {
        Result<Void> result = Result.success();

        assertThat(result.getCode()).isEqualTo(200);
        assertThat(result.getMessage()).isEqualTo("Success");
        assertThat(result.getData()).isNull();
    }

    @Test
    void successShouldSupportExplicitMetadata() {
        Result<String> result = Result.success(201, "Created", "payload");

        assertThat(result.getCode()).isEqualTo(201);
        assertThat(result.getMessage()).isEqualTo("Created");
        assertThat(result.getData()).isEqualTo("payload");
    }

    @Test
    void failShouldSupportOptionalPayload() {
        Result<String> result = Result.fail(400, "Bad Request", "details");
        Result<Object> noPayload = Result.fail(500, "Boom");

        assertThat(result.getCode()).isEqualTo(400);
        assertThat(result.getMessage()).isEqualTo("Bad Request");
        assertThat(result.getData()).isEqualTo("details");
        assertThat(noPayload.getCode()).isEqualTo(500);
        assertThat(noPayload.getMessage()).isEqualTo("Boom");
        assertThat(noPayload.getData()).isNull();
    }

    @Test
    void constructorsAndBuilderShouldPopulateFields() {
        Result<String> constructed = new Result<>(202, "Accepted", "value");
        Result<String> built = Result.<String>builder()
                .code(204)
                .message("No Content")
                .data(null)
                .build();

        assertThat(constructed.getCode()).isEqualTo(202);
        assertThat(constructed.getMessage()).isEqualTo("Accepted");
        assertThat(constructed.getData()).isEqualTo("value");
        assertThat(built.getCode()).isEqualTo(204);
        assertThat(built.getMessage()).isEqualTo("No Content");
    }
}
package com.example.loopa.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private String message;
    private T data;
    private boolean success;

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(message, data, true);
    }

    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(message, null, true);
    }

    public static <T> ApiResponse<T> fail(String message) {
        return new ApiResponse<>(message, null, false);
    }
}

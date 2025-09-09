package com.thanhnb.englishlearningplatform.dto.response;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class ApiResponse<T> {
    private String message;
    private T data;
    private Boolean success;

    public static <T> ApiResponse<T> success(T data){
        return ApiResponse.<T>builder()
        .message("Thành công") 
        .data(data)
        .success(true)
        .build();
    }

    public static <T> ApiResponse<T> success(String message, T data){
        return ApiResponse.<T>builder()
        .message(message) 
        .data(data)
        .success(true)
        .build();
    }

    public static <T> ApiResponse<T> error(String message){
        return ApiResponse.<T>builder()
        .message(message) 
        .data(null)
        .success(false)
        .build();
    }
}

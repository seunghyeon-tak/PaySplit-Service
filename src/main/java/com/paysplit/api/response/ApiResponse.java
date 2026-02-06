package com.paysplit.api.response;

public class ApiResponse<T> {
    private final boolean success;
    private final String message;
    private final T data;

    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    // 성공 - 데이터 없음
    public static ApiResponse<Void> success() {
        return new ApiResponse<>(true, "SUCCESS", null);
    }

    // 성공 - 데이터 있음
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "SUCCESS", data);
    }

    // 실패 - 메시지만
    public static ApiResponse<Void> error(String message) {
        return new ApiResponse<>(false, message, null);
    }

    // 실패 - 데이터 포함
    public static <T> ApiResponse<T> error(String message, T data) {
        return new ApiResponse<>(false, message, data);
    }
}

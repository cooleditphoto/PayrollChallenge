package com.muziqiu.payrollapplication.util;

public enum ResultCode {
    SUCCESS(100, "Request is successful"),
    FAIL(101, "Request is unsuccessful");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }

}

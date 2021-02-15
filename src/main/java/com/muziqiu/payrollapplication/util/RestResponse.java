package com.muziqiu.payrollapplication.util;

import org.springframework.stereotype.Component;

@Component
public class RestResponse {
    int statusCode;
    String message;
    private Object data;

    public RestResponse() {

    }

    public static RestResponse succuess() {
        RestResponse restResponse = new RestResponse();
        restResponse.setResultCode(ResultCode.SUCCESS);

        return restResponse;
    }

    public static RestResponse succuess(Object data) {
        RestResponse restResponse = new RestResponse();
        restResponse.setResultCode(ResultCode.SUCCESS);
        restResponse.setData(data);

        return restResponse;
    }

    public static RestResponse fail() {
        RestResponse restResponse = new RestResponse();
        restResponse.setResultCode(ResultCode.FAIL);

        return restResponse;
    }


    public static RestResponse fail(ResultCode resultCode) {
        RestResponse restResponse = new RestResponse();
        restResponse.setResultCode(resultCode);

        return restResponse;
    }

    public static RestResponse fail(String message) {
        RestResponse restResponse = new RestResponse();
        restResponse.setStatusCode(ResultCode.FAIL.code());
        restResponse.setMessage(message);

        return restResponse;
    }

    public static RestResponse fail(Integer code, String message) {
        RestResponse restResponse = new RestResponse();
        restResponse.setStatusCode(code);
        restResponse.setMessage(message);

        return restResponse;
    }

    public static RestResponse fail(ResultCode resultCode, Object data) {
        RestResponse restResponse = new RestResponse();
        restResponse.setResultCode(resultCode);
        restResponse.setData(data);

        return restResponse;
    }

    private void setResultCode(ResultCode resultCode) {
        this.statusCode = resultCode.code();
        this.message = resultCode.message();
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

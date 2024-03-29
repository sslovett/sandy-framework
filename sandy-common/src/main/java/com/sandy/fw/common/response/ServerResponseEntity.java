package com.sandy.fw.common.response;

import lombok.Data;
import java.io.Serializable;

@Data
public class ServerResponseEntity<T> implements Serializable {

    private String code;

    private String msg;

    private T data;

//    private String version = "1.0.0";
//
//    private Long timestamp;
//
//    private String sign;

    public static <T> ServerResponseEntity<T> success(T data) {
        ServerResponseEntity<T> responseEntity = new ServerResponseEntity<T>();
        responseEntity.setCode(ResponseEnum.SUCCESS.value());
        responseEntity.setMsg(ResponseEnum.SUCCESS.msg());
        responseEntity.setData(data);
        return responseEntity;
    }

    public static <T> ServerResponseEntity<T> success() {
        ServerResponseEntity<T> responseEntity = new ServerResponseEntity<T>();
        responseEntity.setCode(ResponseEnum.SUCCESS.value());
        responseEntity.setMsg(ResponseEnum.SUCCESS.msg());
        return responseEntity;
    }

    public static <T> ServerResponseEntity<T> fail(String msg) {
        ServerResponseEntity<T> responseEntity = new ServerResponseEntity<T>();
        responseEntity.setCode(ResponseEnum.FAIL.value());
        responseEntity.setMsg(msg);
        return responseEntity;
    }

    public static <T> ServerResponseEntity<T> fail(ResponseEnum responseEnum) {
        ServerResponseEntity<T> responseEntity = new ServerResponseEntity<T>();
        responseEntity.setCode(responseEnum.value());
        responseEntity.setMsg(responseEnum.msg());
        return responseEntity;
    }

    public static <T> ServerResponseEntity<T> fail(String code, String msg) {
        ServerResponseEntity<T> responseEntity = new ServerResponseEntity<T>();
        responseEntity.setCode(code);
        responseEntity.setMsg(msg);
        return responseEntity;
    }
}

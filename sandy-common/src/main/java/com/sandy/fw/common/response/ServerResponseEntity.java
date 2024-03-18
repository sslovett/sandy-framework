package com.sandy.fw.common.response;

import lombok.Data;
import java.io.Serializable;

@Data
public class ServerResponseEntity<T> implements Serializable {

    private String code;

    private String msg;

    private T data;

    private String version = "1.0.0";

    private Long timestamp;

    private String sign;

    public static <T> ServerResponseEntity<T> success(T data) {
        ServerResponseEntity<T> responseEntity = new ServerResponseEntity<T>();
        responseEntity.setCode(ResponseEnum.SUCCESS.value());
        responseEntity.setMsg(ResponseEnum.SUCCESS.msg());
        responseEntity.setData(data);
        return responseEntity;
    }
}

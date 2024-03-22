package com.sandy.fw.common.response;


public enum ResponseEnum {

    SUCCESS("00000", "success"),
    FAIL("00001", "fail"),
    ERROR("00002", "服务器开小差了"),
    PARAM_ERROR("00003", "参数错误"),
    UNAUTHORIZED("00004", "认证未通过"),
    UN_ACCESS_DENIED("00005", "无权限访问");

    private final String code;
    private final String msg;

    ResponseEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String value() {
        return code;
    }

    public String msg() {
        return msg;
    }
}

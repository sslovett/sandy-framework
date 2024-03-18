package com.sandy.fw.common.response;


public enum ResponseEnum {

    SUCCESS("00000", "success"),
    FAIL("00001", "fail"),
    ERROR("00002", "服务器开小差了"),
    PARAM_ERROR("00003", "参数错误"),
    UNAUTHORIZED("00004", "授权验证未通过");

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

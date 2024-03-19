package com.sandy.fw.common.exception;

import com.sandy.fw.common.response.ResponseEnum;
import lombok.Data;

@Data
public class DefaultException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private String code;

    public DefaultException(String msg) {
        super(msg);
        this.code = ResponseEnum.FAIL.value();
    }
}

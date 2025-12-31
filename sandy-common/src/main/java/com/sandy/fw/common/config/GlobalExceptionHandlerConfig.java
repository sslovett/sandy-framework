package com.sandy.fw.common.config;

import com.sandy.fw.common.exception.DefaultException;
import com.sandy.fw.common.response.ResponseEnum;
import com.sandy.fw.common.response.ServerResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import javax.validation.ValidationException;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandlerConfig {

    @ExceptionHandler({ValidationException.class, BindException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<ServerResponseEntity<?>> handleValidatedException(Exception e) {
        log.error("handleValidatedException", e);
        String defaultMessages;
        if(e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException ex = (MethodArgumentNotValidException) e;
            defaultMessages = ex.getBindingResult().getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining("; "));
        } else if(e instanceof BindException) {
            BindException ex = (BindException) e;
            defaultMessages = ex.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining("; "));
        } else {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ServerResponseEntity.fail(ResponseEnum.PARAM_ERROR));
        }

        defaultMessages = ResponseEnum.PARAM_ERROR.msg() + ": " + defaultMessages;

        return ResponseEntity.status(HttpStatus.OK)
                .body(ServerResponseEntity.fail(ResponseEnum.PARAM_ERROR.value(), defaultMessages));
    }

    @ExceptionHandler({DefaultException.class})
    public ResponseEntity<ServerResponseEntity<?>> defaultExceptionHandler(DefaultException e) {
        log.error("defaultExceptionHandler", e);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ServerResponseEntity.fail(e.getCode(), e.getMessage()));
    }

//    @ExceptionHandler({Exception.class})
//    public ResponseEntity<ServerResponseEntity<?>> exceptionHandler(Exception e) {
//        log.error("exceptionHandler", e);
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(ServerResponseEntity.fail(ResponseEnum.ERROR));
//    }
}

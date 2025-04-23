package com.ruoyi.common.exception;

/**
 * 不进行捕获的异常
 */
public class NonCaptureException extends RuntimeException {
    public NonCaptureException(String message, Throwable cause) {
        super(message, cause);
    }
}

package com.yintu.ruixing.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 验证码异常类
 */
public class VerificationCodeException extends AuthenticationException {

    private static final long serialVersionUID = -7349386769326122346L;

    public VerificationCodeException(String msg) {
        super(msg);
    }

    public VerificationCodeException(String msg, Throwable t) {
        super(msg, t);
    }
}

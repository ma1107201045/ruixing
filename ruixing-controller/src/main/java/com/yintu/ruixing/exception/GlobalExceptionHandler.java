package com.yintu.ruixing.exception;

import com.yintu.ruixing.common.util.ResponseDataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Map;

/**
 * @author:mlf
 * @date:2020/5/19 17:14
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    public static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(SQLException.class)
    public Map<String, Object> sqlException(SQLException e) {
        logger.error(e.getMessage());
        if (e instanceof SQLIntegrityConstraintViolationException) {
            return ResponseDataUtil.error("该数据有关联数据，操作失败");
        }
        return ResponseDataUtil.error("数据库异常，操作失败");
    }
}

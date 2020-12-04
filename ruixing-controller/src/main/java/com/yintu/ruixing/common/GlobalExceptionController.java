package com.yintu.ruixing.common;

import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author:mlf
 * @date:2020/5/19 17:14
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionController {


    @ExceptionHandler(BindException.class)
    public Map<String, Object> bindException(BindException e) {
        StringBuilder sb = new StringBuilder();
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            sb.append(fieldError.getField()).append("：").append(fieldError.getDefaultMessage()).append(" ");
        }
        return ResponseDataUtil.error(sb.toString());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Map<String, Object> maxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        log.error(e.getMessage());
        return ResponseDataUtil.error("文件上传异常，文件过大");
    }

    @ExceptionHandler(SQLException.class)
    public Map<String, Object> sqlException(SQLException e) {
        if (e instanceof SQLIntegrityConstraintViolationException) {
            return ResponseDataUtil.error("车站id有重复,请检查车站id,操作失败");
        }
        log.error("" + e.getMessage());
        return ResponseDataUtil.error("数据库异常，操作失败(" + e.getMessage() + ")");
    }

    @ExceptionHandler(BaseRuntimeException.class)
    public Map<String, Object> baseRuntimeException(BaseRuntimeException e) {
        log.error(e.getMessage());
        return ResponseDataUtil.error(e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public Map<String, Object> runtimeException(RuntimeException e) {
        if (e instanceof NullPointerException) {
            return ResponseDataUtil.error("空指针异常");
        }
        log.error(e.getMessage());
        return ResponseDataUtil.error(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Map<String, Object> exception(Exception e) {
        log.error(e.getMessage());
        return ResponseDataUtil.error(e.getMessage());
    }


}

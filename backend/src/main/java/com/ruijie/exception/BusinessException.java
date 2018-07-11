package com.ruijie.exception;


import com.ruijie.enums.ErrorCode;

/**
 * 业务异常捕获
 *
 * @author zql
 */
@SuppressWarnings("serial")
public class BusinessException extends RuntimeException {

    private Object rspObj;

    private ErrorCode code;

    public BusinessException(ErrorCode code) {
        super();
        this.code = code;
        rspObj = code.getMsg();
    }

    public BusinessException(ErrorCode code, Object rspObj) {
        super();
        this.code = code;
        this.rspObj = rspObj;
    }

    public Object getRspObj() {
        return rspObj;
    }

    public void setRspObj(Object rspObj) {
        this.rspObj = rspObj;
    }

    public ErrorCode getCode() {
        return code;
    }

    public void setCode(ErrorCode code) {
        this.code = code;
    }
}

package com.pay.third.commons.system;

/**
 * 自定义异常
 *
 * @author Tang
 * @version 1.0.0
 * @date 2021/3/30 17:36
 */
public class CustomException extends RuntimeException {

    private static final long serialVersionUID = 3378262392908773997L;
    private int code;
    private int subCode;
    private String[] params;

    public CustomException(int code) {
        this.code = code;
    }

    public CustomException(int code, int subCode) {
        this.code = code;
        this.subCode = subCode;
    }

    public CustomException(int code, int subCode, String[] params) {
        this.code = code;
        this.subCode = subCode;
        this.params = params;
    }

    public CustomException(String message) {
        super(message);
    }

    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomException(int code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public CustomException(int code, String message) {
        super(message);
        this.code = code;
    }

    public CustomException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getSubCode() {
        return subCode;
    }

    public void setSubCode(int subCode) {
        this.subCode = subCode;
    }

    public String[] getParams() {
        return params;
    }

    public void setParams(String[] params) {
        this.params = params;
    }
}

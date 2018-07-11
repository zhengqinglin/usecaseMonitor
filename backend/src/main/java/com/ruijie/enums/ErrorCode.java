package com.ruijie.enums;


/**
 * @author zhengqinglin
 * @date 2017/9/26
 */
public enum ErrorCode {

    REQUIRE_ARGUMENT(1000, "参数缺失"),
    EXPIRED_TIMESTAMP(1001, "时间戳过期"),
    INVALID_SIGN(1002, "签名错误"),
    INVALID_IP(1003, "IP不可信"),
    EXCEED_QUOTA(1004, "访问次数超过限额"),
    INVALID_FILE_FORMAT(1005, "文件格式不正确"),
    FILE_NOT_FOUND(1006, "文件不存在"),
    IDENTIFY_MISMATCH(1007, "机器码不匹配"),
    EXPIRED_TRIAL(1008, "试用超期"),
    FAILURE_COLLECT_HW(1009, "收集硬件码失败"),
    FAILURE_ENCRYPT_HW(1010, "对硬件码进行加密失败"),
    FAILURE_PARSE_FILE(1011, "文件解析失败"),
    FAILURE_COPY_FILE(1012, "文件复制失败"),
    FAILURE_DELETE_FILE(1013, "文件删除失败"),
    SN_MISMATCH(1014, "授权码不匹配"),
    FILE_EXIST(1015, "授权文件已存在"),
    INTERNAL_SERVER_ERROR(500, "内部服务器异常");


    private int code;
    private String msg;

    public static ErrorCode getErrorEnum(int code) {
        for (ErrorCode e : ErrorCode.values()) {
            if (e.code == code) {
                return e;
            }
        }
        return null;
    }

    ErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

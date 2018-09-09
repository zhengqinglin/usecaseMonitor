package com.ruijie.enums;

public enum ExcelType {
    xls("xls"),
    xlsx("xlsx"),
    xlsm("xlsm");

    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    ExcelType(String code) {
        this.code = code;
    }

    public static ExcelType resolve(String code) {
        return ExcelType.valueOf(code);
    }
}

package com.ruijie.util;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;
import java.util.Set;

/**
 * Created by zhengqinglin on 2018/6/16.
 */
public class ExcelUtils {

    protected static final Logger LOG = LoggerFactory.getLogger(ExcelUtils.class);

    public static Map<String,Object> getBasicInfo(String filePath) {
        Map<String,Object> resultMap = Maps.newHashMap();
        try {
            String userCasePackageName = filePath.split(".xls")[0];
            resultMap.put("userCasePackageName",userCasePackageName);
            Workbook wb = new XSSFWorkbook(new FileInputStream(filePath));
            for(Sheet one : wb) {
                Set<Integer> prioritySet = Sets.newHashSet();
                if (!one.getSheetName().contains("模块")) {
                    continue;
                }
                for(Row row : one) {
                    for(Cell cell : row) {
                        if (cell.getColumnIndex() == 6 && cell.getCellTypeEnum() == CellType.NUMERIC) {
                            prioritySet.add((int)cell.getNumericCellValue());
                        }
                    }
                }
                resultMap.put(one.getSheetName(),prioritySet);
            }

        } catch (Exception e) {
            LOG.error("出错了",e);
        }
        return resultMap;
    }

    public static void findResult(String filePath, String moduleName, Set<Integer> priority) {
        Map<String,Object> resultMap = Maps.newHashMap();
        try {
            String userCasePackageName = filePath.split(".xls")[0];
            resultMap.put("userCasePackageName",userCasePackageName);
            Workbook wb = new XSSFWorkbook(new FileInputStream(filePath));
            for(Sheet one : wb) {
                if (!one.getSheetName().contains(moduleName)) {
                    continue;
                }
                int total = 0;
                int pass = 0;
                int fail = 0;
                for(Row row : one) {
                    for(Cell cell : row) {
                        //读取优先级列的数据
                        if (cell.getColumnIndex() == 6 && cell.getCellTypeEnum() == CellType.NUMERIC && priority.contains((int)cell.getNumericCellValue())) {
                            //用例总数、pass or fail、已执行条数
                            total++;
                        }
                        //读取执行结果列的数据
                        if (cell.getColumnIndex() == 15 && cell.getRowIndex() != 0) {
                            //判断测试结果是pass或者是fail
                            if (StringUtils.equalsIgnoreCase(cell.getStringCellValue(),"pass")) {
                                pass++;
                            } else if (StringUtils.equalsIgnoreCase(cell.getStringCellValue(),"fail"))  {
                                fail++;
                            } else {
                                //记录错误
                                LOG.warn("文件{}的{}的第{}行的第{}列出现异常输入:{}",filePath,moduleName,cell.getRowIndex(),cell.getColumnIndex(),cell.toString());
                            }
                        }
                    }
                }
                System.out.println(total+"-"+pass+"-"+fail);
            }
        } catch (Exception e) {
            LOG.error("出错了",e);
        }
//        return resultMap;
    }

    public static void testReadExcel(String filePath) {
        try {
            //用例名称：读取文件名称，去掉后缀
            //功能模块：读取sheet的名称
            //用例级别：读取sheet的优先级字段7，字符串“0123”
            // 读取Excel
            //根据文件后缀，选择不同的类
            Workbook wb = new XSSFWorkbook(new FileInputStream(filePath));

            // 获取sheet数目
            for (int t = 0; t < wb.getNumberOfSheets(); t++) {
                Sheet sheet = wb.getSheetAt(t);
                sheet.getSheetName();
                Row row = null;
                int lastRowNum = sheet.getLastRowNum();

                // 循环读取
                for (int i = 0; i <= lastRowNum; i++) {
                    row = sheet.getRow(i);
                    if (row != null) {
                        // 获取每一列的值
                        for (int j = 0; j < row.getLastCellNum(); j++) {
                            Cell cell = row.getCell(j);
                            String value = getCellValue(cell) ;
                            if(!value.equals("")){
                                System.out.print(value + " | ");
                            }
                        }
                        System.out.println();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * 读取单元格的值
     *
     * @Title: getCellValue
     * @Date : 2014-9-11 上午10:52:07
     * @param cell
     * @return
     */
    private static String getCellValue(Cell cell) {
        Object result = "";
        if (cell != null) {
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_STRING:
                    result = cell.getStringCellValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    result = cell.getNumericCellValue();
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    result = cell.getBooleanCellValue();
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    result = cell.getCellFormula();
                    break;
                case Cell.CELL_TYPE_ERROR:
                    result = cell.getErrorCellValue();
                    break;
                case Cell.CELL_TYPE_BLANK:
                    break;
                default:
                    break;
            }
        }
        return result.toString();
    }

    public static void main(String[] args) {
//        ExcelUtils.testReadExcel("D:/主课CM学生端-GN-TP.XLSM");
//        ExcelUtils.getBasicInfo("D:/主课CM学生端-GN-TP.XLSM");
        Set<Integer> prioritySet = Sets.newHashSet();
        prioritySet.add(0);
        prioritySet.add(1);
        prioritySet.add(2);
        prioritySet.add(3);
        ExcelUtils.findResult("D:/主课CM学生端-GN-TP.XLSM","XXX模块",prioritySet);
    }
}

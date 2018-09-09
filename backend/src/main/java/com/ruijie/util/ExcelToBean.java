package com.ruijie.util;

import com.google.common.io.Files;
import com.ruijie.enums.ExcelType;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author zql
 * @description:
 * @date: 16:50 2018/6/30
 */
public class ExcelToBean {

    private static Logger LOG = LoggerFactory.getLogger(ExcelToBean.class);

    private static ResourceBundle modelPropertiesBundle;

    private static String excludeSheetNameSet = "测试拓扑|封面|拆解图|帮助|SPEC表|测试准备";

    static {
        //首次加载该类时加载model.properites文件资源
        modelPropertiesBundle = ResourceBundle.getBundle("model");
    }

    /**
     * 根据文件的后缀，匹配解析类
     */
    public static Workbook resolveExcelFileType(File file) {
        String fileType = Files.getFileExtension(file.getName());
        ExcelType excelType = ExcelType.resolve(fileType.toLowerCase());
        Workbook result = null;
        try {
            switch (excelType) {
                case xls:
                    result = new HSSFWorkbook((new FileInputStream(file)));
                    break;
                case xlsm:
                case xlsx:
                    result = new XSSFWorkbook((new FileInputStream(file)));
                    break;
                default:
                    LOG.error("当前文件类型{}不支持", fileType);
            }
        } catch (IOException e) {
            LOG.error("解析文件{}出现异常", file);
        }
        return result;
    }

    /**
     * 将workbook中的值放入List<Map<String,Object>>结构中
     */
    public static List<Map<String, Object>> parseExcel(Workbook workbook) {
        List<Map<String, Object>> result = new LinkedList<>();
        int excelRowLength = workbook.getSheetAt(1).getRow(0).getPhysicalNumberOfCells();
        //相应的javabean类的属性名称数组
        String[] columnName = new String[excelRowLength];
        for (int i = 0; i < columnName.length; i++) {
            //从资源文件中获取
            if (modelPropertiesBundle.containsKey((String.valueOf(i)))) {
                columnName[i] = modelPropertiesBundle.getString(String.valueOf(i));
            }
        }
        //sheet
        for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            if (excludeSheetNameSet.contains(sheet.getSheetName())) {
                continue;
            }
            //row
            int cellLength = sheet.getRow(0).getLastCellNum();
            for (int rowIndex = 1; rowIndex < sheet.getPhysicalNumberOfRows(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (null == row || null == row.getCell(0) || StringUtils.isBlank(row.getCell(0).getStringCellValue())) {
                    continue;
                }
                Map<String, Object> map = new HashMap<>();
                for (int cellIndex = 0; cellIndex < cellLength; cellIndex++) {
                    Cell cell = row.getCell(cellIndex);
                    //该列值在对应的java对象中有值
                    if (columnName[cellIndex] != null && columnName[cellIndex].trim().length() > 0) {
                        //取出当前cell的值和对应Javabean类的属性放入到map中
                        map.put(columnName[cellIndex].trim(), getCellValue(cell));
                    }
                }
                if (map.size() == 0) {
                    continue;
                }
                result.add(map);
            }
        }
        return result;
    }

    public static List<Map<String, Object>> parseExcelByParam(Workbook workbook, String sheetName) {
        List<Map<String, Object>> result = new LinkedList<>();
        //相应的javabean类的属性名称数组
        String[] columnName = new String[22];
        for (int i = 0; i < columnName.length; i++) {
            //从资源文件中获取
            if (modelPropertiesBundle.containsKey((String.valueOf(i)))) {
                columnName[i] = modelPropertiesBundle.getString(String.valueOf(i));
            }
        }
        //sheet
        for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            if (excludeSheetNameSet.contains(sheet.getSheetName())) {
                continue;
            }
            if (!sheetName.equalsIgnoreCase(sheet.getSheetName())) {
                continue;
            }
            //row
            int cellLength = sheet.getRow(0).getLastCellNum();
            for (int rowIndex = 1; rowIndex < sheet.getPhysicalNumberOfRows(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (null == row || null == row.getCell(0) || StringUtils.isBlank(row.getCell(0).getStringCellValue())) {
                    continue;
                }
                Map<String, Object> map = new HashMap<>();
                for (int cellIndex = 0; cellIndex < cellLength; cellIndex++) {
                    Cell cell = row.getCell(cellIndex);
                    //该列值在对应的java对象中有值
                    if (columnName[cellIndex] != null && columnName[cellIndex].trim().length() > 0) {
                        //取出当前cell的值和对应Javabean类的属性放入到map中
                        map.put(columnName[cellIndex].trim(), getCellValue(cell));
                    }
                }
                if (map.size() == 0) {
                    continue;
                }
                result.add(map);
            }
        }
        return result;
    }

    /**
     * 将workbook中的值放入List<Map<String,Object>>结构中
     */
    public static List<String> parseExcelForSheetName(Workbook workbook) {
        List<String> result = new LinkedList<>();
        //sheet
        for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            if (excludeSheetNameSet.contains(sheet.getSheetName())) {
                continue;
            }

            result.add(sheet.getSheetName());
        }
        return result;
    }


    /**
     * 利用反射将    List<Map<String,Object>>结构 生成相应的List<T>数据
     */
    public static <T> List<T> toObjectList(List<Map<String, Object>> list, Class<T> clazz) throws Exception {
        List<T> returnList = new LinkedList<>();
        for (int i = 0; i < list.size(); i++) {
            Set<Map.Entry<String, Object>> set = list.get(i).entrySet();
            Iterator<Entry<String, Object>> it = set.iterator();
            T obj = clazz.newInstance();
            Method[] methods = clazz.getDeclaredMethods();
            //生成一个obj
            while (it.hasNext()) {
                Map.Entry<String, Object> entry = it.next();
                for (Method m : methods) {
                    if (m.getName().startsWith("set")) {
                        //为obj赋值
                        String methodName = entry.getKey();
                        StringBuffer sb = new StringBuffer(methodName);
                        sb.replace(0, 1, (methodName.charAt(0) + "").toUpperCase());
                        methodName = "set" + sb.toString();
                        if (methodName.equals(m.getName())) {
                            m.invoke(obj, entry.getValue());
                            break;
                        }
                    }
                }
            }
            returnList.add(obj);
        }
        return returnList;
    }


    /**
     * 获取当前单元格内容
     */
    private static Object getCellValue(Cell cell) {
        Object value = "";
        if (cell != null) {
            switch (cell.getCellTypeEnum()) {
                case NUMERIC:
                    //日期类型
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = HSSFDateUtil.getJavaDate(cell.getNumericCellValue());
                        value = sdf.format(date);
                    } else {
                        Double data = cell.getNumericCellValue();
                        value = data.toString();
                    }
                    break;
                case STRING:
                    value = cell.getStringCellValue();
                    break;
                case BOOLEAN:
                    value = cell.getBooleanCellValue();
                    break;
                case ERROR:
                    LOG.error("单元格内容出现错误");
                    break;
                case FORMULA:
                    value = String.valueOf(cell.getNumericCellValue());
                    // 如果获取的数据值非法,就将其装换为对应的字符串
                    if (value.equals("NaN")) {
                        value = cell.getStringCellValue();
                    }
                    break;
                case BLANK:
                    LOG.warn("单元格内容 为空值 ");
                    break;
                default:
                    value = cell.getStringCellValue();
                    break;
            }
        }
        return value;
    }
}

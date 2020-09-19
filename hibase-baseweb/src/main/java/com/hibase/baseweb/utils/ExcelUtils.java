package com.hibase.baseweb.utils;

import cn.hutool.core.collection.CollUtil;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.*;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * excel解析工具类
 *
 * @author chenfeng
 * @create 2018-08-17 14:45
 */
public class ExcelUtils {

    /**
     * 根据流读取第一个excel文件
     *
     * @param inputStream
     * @return
     */
    public static List<List<String>> parseFirstExcelFile(InputStream inputStream) throws Exception {

        List<Sheet> sheets = readExcelFile(inputStream, 0);

        return readExcelFile(sheets);
    }

    /**
     * 根据流读取所有excel文件
     *
     * @param inputStream
     * @return
     */
    public static List<List<String>> parseAllExcelFile(InputStream inputStream) throws Exception {

        List<Sheet> sheets = readExcelFile(inputStream, null);

        return readExcelFile(sheets);
    }

    /**
     * 根据流读取所有excel文件
     *
     * @param inputStream
     * @return
     */
    public static List<List<String>> parseExcelFile(InputStream inputStream, Integer assignExcel) throws Exception {

        List<Sheet> sheets = readExcelFile(inputStream, assignExcel);

        return readExcelFile(sheets);
    }

    /**
     * 根据流生成多个excel文件，并指定只解析哪一张表格
     *
     * @param inputStream
     * @param assignExcel 指定第几张excel，从0开始
     * @return
     * @throws Exception
     */
    public static List<Sheet> readExcelFile(InputStream inputStream, Integer assignExcel) throws Exception {

        List<Sheet> result = new ArrayList<>();

        try {

            Workbook workbook = WorkbookFactory.create(inputStream);

            if (workbook != null && workbook.getNumberOfSheets() > 0) {

                if (assignExcel != null && assignExcel > workbook.getNumberOfSheets()) {

                    return result;
                }

                for (int i = 0; i < workbook.getNumberOfSheets(); i++) {

                    if (assignExcel == null) {

                        result.add(workbook.getSheetAt(i));
                    } else {

                        result.add(workbook.getSheetAt(assignExcel));
                        return result;
                    }
                }
            }
        } catch (Exception e) {

            if (inputStream != null) {

                inputStream.close();
            }

            throw e;
        }

        return result;
    }

    /**
     * 解析表格文件
     *
     * @param sheets
     * @return
     * @throws Exception
     */
    public static List<List<String>> readExcelFile(List<Sheet> sheets) throws Exception {

        List<List<String>> result = new ArrayList<>();

        if (CollUtil.isNotEmpty(sheets)) {

            for (Sheet sheet : sheets) {

                int rowNum = sheet.getLastRowNum() + 1;
                for (int i = 0; i < rowNum; i++) {

                    Row row = sheet.getRow(i);

                    List<String> lineValues = new ArrayList<>();

                    if (row != null) {

                        for (int j = 0; j < row.getLastCellNum(); j++) {

                            Cell cell = row.getCell(j);
                            // 获取单元格的值
                            String str = getCellValue(cell);
                            // 将得到的值放入列表中
                            lineValues.add(str);
                        }
                    }

                    result.add(lineValues);
                }
            }
        }

        return result;
    }

    //获取单元格的值
    private static String getCellValue(Cell cell) {

        String cellValue = "";
        if (cell != null) {
            // 判断单元格数据的类型，不同类型调用不同的方法
            switch (cell.getCellTypeEnum()) {
                // 数值类型
                case NUMERIC:
                    // 进一步判断 ，单元格格式是日期格式
                    if (DateUtil.isCellDateFormatted(cell)) {

                        cellValue = MyUtils.changeTimeByParam(cell.getDateCellValue(), MyUtils.FORMAT_YYYY_MM_DD_HH_MM_SS);
                    } else {
                        //数值
                        double value = cell.getNumericCellValue();
                        DecimalFormat df = new DecimalFormat("#.###");
                        cellValue = df.format(value);
                    }
                    break;
                case STRING:
                    cellValue = cell.getStringCellValue();
                    break;
                case BOOLEAN:
                    cellValue = String.valueOf(cell.getBooleanCellValue());
                    break;
                //判断单元格是公式格式，需要做一种特殊处理来得到相应的值
                case FORMULA:
                    try {
                        cellValue = String.valueOf(cell.getNumericCellValue());
                    } catch (IllegalStateException e) {
                        cellValue = String.valueOf(cell.getRichStringCellValue());
                    }
                    break;
                case BLANK:
                    cellValue = "";
                    break;
                case ERROR:
                    cellValue = "";
                    break;
                default:
                    cellValue = cell.toString().trim();
                    break;
            }
        }
        return cellValue.trim();
    }

    public static void main(String[] args) {

        try {

//            InputStream inputStream = new FileInputStream("C:\\Users\\Administrator\\Desktop\\年审测试.xls");
//
//            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
//
//            List<Sheet> sheets = readExcelFile(bufferedInputStream, 0);
//
//            List<List<String>> bb = readExcelFile(sheets);
            double value = 20000.100;
            DecimalFormat df = new DecimalFormat("#.###");
            String cellValue = df.format(value);

            System.out.println(cellValue);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}

package com.automation.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

public class ExcelUtils {

    private String filePath;
    private Workbook workbook;
    private Sheet sheet;

    public ExcelUtils(String filePath) {
        this.filePath = filePath;
        try {
            FileInputStream fis = new FileInputStream(filePath);
            workbook = new XSSFWorkbook(fis);
            sheet = workbook.getSheetAt(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getEmail() {
        Row row = sheet.getRow(1);
        String prefix = row.getCell(0).getStringCellValue();
        int count = (int) row.getCell(2).getNumericCellValue();
        return prefix+"+test" + count + "@gmail.com";
    }

    public String getPassword() {
        Row row = sheet.getRow(1);
        return row.getCell(1).getStringCellValue();
    }

    public void incrementCount() {
        try {
            Row row = sheet.getRow(1);
            Cell cell = row.getCell(2);
            int count = (int) cell.getNumericCellValue();
            cell.setCellValue(count + 1);

            FileOutputStream fos = new FileOutputStream(filePath);
            workbook.write(fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeWorkbook() {
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
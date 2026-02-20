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
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns an email like "prefix+test<count>@gmail.com"
     * and increments the count in the Excel file.
     */
    public String getEmail() {
        try {
            Row row = sheet.getRow(1);
            if (row == null) throw new RuntimeException("Row 2 not found in Excel");

            String prefix = getCellValueAsString(row.getCell(0));
            int count = getCellValueAsInt(row.getCell(2));


            return prefix + "+test" + count + "@gmail.com";

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns the password from Excel
     */
    public String getPassword() {
        Row row = sheet.getRow(1);
        if (row == null) throw new RuntimeException("Row 2 not found in Excel");
        return getCellValueAsString(row.getCell(1));
    }

    /**
     * Helper method: safely get numeric value from a cell
     */
    private int getCellValueAsInt(Cell cell) {
        if (cell == null) return 0;
        switch (cell.getCellType()) {
            case NUMERIC:
                return (int) cell.getNumericCellValue();
            case STRING:
                try {
                    return Integer.parseInt(cell.getStringCellValue());
                } catch (NumberFormatException e) {
                    return 0;
                }
            default:
                return 0;
        }
    }

    /**
     * Helper method: safely get string value from a cell
     */
    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }

    /**
     * Save the workbook back to the file
     */
    private void saveWorkbook() {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            workbook.write(fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Close the workbook
     */
    public void closeWorkbook() {
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
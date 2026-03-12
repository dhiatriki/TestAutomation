package com.automation.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

public class ExcelUtils {

    private static final int HEADER_ROW_INDEX = 0;
    private static final int DATA_ROW_INDEX = 1;

    private final String filePath;
    private Workbook workbook;
    private Sheet sheet;

    public ExcelUtils(String filePath) {
        this.filePath = new File(filePath).getAbsolutePath();
        openWorkbook();
    }

    private void openWorkbook() {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            workbook = new XSSFWorkbook(fis);
            sheet = workbook.getSheetAt(0);
        } catch (IOException e) {
            throw new RuntimeException("Unable to open Excel file: " + filePath, e);
        }
    }

    private void ensureWorkbookOpen() {
        if (workbook == null || sheet == null) {
            openWorkbook();
        }
    }

    private Row getDataRow() {
        ensureWorkbookOpen();
        Row row = sheet.getRow(DATA_ROW_INDEX);
        if (row == null) {
            throw new RuntimeException("Row 2 not found in Excel");
        }
        return row;
    }

    private int getColumnIndex(String headerName) {
        ensureWorkbookOpen();
        Row headerRow = sheet.getRow(HEADER_ROW_INDEX);
        if (headerRow == null) {
            throw new RuntimeException("Header row not found in Excel");
        }

        for (int i = headerRow.getFirstCellNum(); i < headerRow.getLastCellNum(); i++) {
            Cell headerCell = headerRow.getCell(i);
            String currentHeader = getCellValueAsString(headerCell).trim();
            if (currentHeader.equalsIgnoreCase(headerName.trim())) {
                return i;
            }
        }

        throw new RuntimeException("Column not found in Excel: " + headerName);
    }

    private String getStringValue(String headerName) {
        Row row = getDataRow();
        return getCellValueAsString(row.getCell(getColumnIndex(headerName)));
    }

    private int getIntValue(String headerName) {
        Row row = getDataRow();
        return getCellValueAsInt(row.getCell(getColumnIndex(headerName)));
    }

    /**
     * Returns an email like "prefix+test<count>@gmail.com"
     */
    public String getEmail() {
        String prefix = getStringValue("Email");
        int count = getCount();
        return prefix + "+test" + count + "@gmail.com";
    }

    /**
     * Returns the password from Excel
     */
    public String getPassword() {
        return getStringValue("Password");
    }

    public int getCount() {
        return getIntValue("Count");
    }

    public String getStreetNumberAndName() {
        return getStringValue("Street number and name");
    }

    public String getCity() {
        return getStringValue("City");
    }

    public String getPostalCode() {
        return getStringValue("Postal code");
    }

    public String getPhoneNumber() {
        return getStringValue("Phone number");
    }

    public String getDateOfBirth() {
        return getStringValue("Birth date");
    }

    public String getSocialInsuranceNumber() {
        return getStringValue("Social security number");
    }

    public String getFamilyMemberName() {
        return getStringValue("Family member name");
    }

    public String getFamilyMemberLastName() {
        return getStringValue("Family member last name");
    }

    public String getFamilyMemberAddress() {
        return getStringValue("Family member address");
    }

    public String getFamilyMemberPhone() {
        return getStringValue("Family member phone number");
    }

    public String getFamilyMemberMail() {
        return getStringValue("Family member mail");
    }

    public String getFamilyMemberCity() {
        return getStringValue("Family member city");
    }

    public String getFamilyMemberProvince() {
        return getStringValue("Family member province");
    }

    public String getFamilyMemberPostalCode() {
        return getStringValue("Family member postal code");
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
                    return Integer.parseInt(cell.getStringCellValue().trim());
                } catch (NumberFormatException e) {
                    return 0;
                }
            case FORMULA:
                return (int) cell.getNumericCellValue();
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
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    /**
     * Returns the lastname (count value) from Excel
     */
    public String getLastname() {
        return String.valueOf(getCount());
    }

    /**
     * Save the workbook back to the file
     */
    private void saveWorkbook() {
        ensureWorkbookOpen();
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            workbook.write(fos);
            fos.flush();
        } catch (IOException e) {
            throw new RuntimeException("Unable to save Excel file: " + filePath, e);
        }
    }

    public void incrementCount() {
        ensureWorkbookOpen();
        Row row = sheet.getRow(DATA_ROW_INDEX);
        if (row == null) {
            row = sheet.createRow(DATA_ROW_INDEX);
        }

        int countColumnIndex = getColumnIndex("Count");
        Cell countCell = row.getCell(countColumnIndex);
        if (countCell == null) {
            countCell = row.createCell(countColumnIndex);
        }

        int currentCount = getCellValueAsInt(countCell);
        int updatedCount = currentCount + 1;
        countCell.setCellValue(updatedCount);

        saveWorkbook();
        System.out.println("[INFO] Excel counter incremented from " + currentCount + " to " + updatedCount + ".");
    }

    /**
     * Close the workbook
     */
    public void closeWorkbook() {
        if (workbook == null) {
            return;
        }

        try {
            workbook.close();
            System.out.println("[INFO] Excel workbook closed: " + filePath);
        } catch (IOException e) {
            throw new RuntimeException("Unable to close Excel file: " + filePath, e);
        } finally {
            workbook = null;
            sheet = null;
        }
    }
}
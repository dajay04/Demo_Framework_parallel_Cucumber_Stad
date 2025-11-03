package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelUtil
{
    private static XSSFWorkbook workbook;
    private static XSSFSheet sheet;
    private static XSSFRow row;
    private static XSSFCell cell;

    // Open the Excel file
    public static void openWorkbook(String xlfile) throws IOException, FileNotFoundException {
        FileInputStream fis = new FileInputStream(xlfile);
        workbook = new XSSFWorkbook(fis);
    }

    // Save and close the workbook
    public static void saveAndClose(String xlfile) throws IOException {
        FileOutputStream fos = new FileOutputStream(xlfile);
        workbook.write(fos);
        fos.close();
        workbook.close();
    }

    // Get row count
    public static int getRowCount(String xlfile, String xlsheet) throws IOException {
        openWorkbook(xlfile);
        sheet = workbook.getSheet(xlsheet);
        int rowCount = sheet.getPhysicalNumberOfRows();
        saveAndClose(xlfile);
        return rowCount;
    }

    // Get cell count in a row
    public static int getCellCount(String xlfile, String xlsheet, int rownum) throws IOException {
        openWorkbook(xlfile);
        sheet = workbook.getSheet(xlsheet);
        row = sheet.getRow(rownum);
        int cellCount = (row != null) ? row.getLastCellNum() : 0;
        saveAndClose(xlfile);
        return cellCount;
    }

    // Get cell data
    public static String getCellData(String xlfile, String xlsheet, int rownum, int colnum) {
        String data = "";
        try (FileInputStream fis = new FileInputStream(xlfile);
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheet(xlsheet);

            if (sheet == null) {
                System.out.println("Sheet not found: " + xlsheet);
                return "";
            }

            Row row = sheet.getRow(rownum);
            if (row == null) {
                System.out.println("Row not found: " + rownum);
                return "";
            }
            Cell cell = row.getCell(colnum);
            if (cell == null) {
                System.out.println("Cell not found: " + colnum);
                return "";
            }

            // Handle different cell types
            switch (cell.getCellType()) {
                case STRING:
                    data = cell.getStringCellValue();
                    break;
                case NUMERIC:
                    data = String.valueOf(cell.getNumericCellValue());
                    break;
                case BOOLEAN:
                    data = String.valueOf(cell.getBooleanCellValue());
                    break;
                case FORMULA:
                    data = cell.getCellFormula();
                    break;
                default:
                    data = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    // Set cell data
    public static void setCellData(String xlfile, String xlsheet, int rownum, int colnum, String data) throws IOException {
        openWorkbook(xlfile);
        sheet = workbook.getSheet(xlsheet);

        if (sheet.getRow(rownum) == null) {
            sheet.createRow(rownum);
        }

        row = sheet.getRow(rownum);
        cell = row.createCell(colnum);
        cell.setCellValue(data);

        saveAndClose(xlfile);
    }

    // Fill cell with green color
    public static void fillGreenColor(String xlfile, String xlsheet, int rownum, int colnum, String data) throws IOException {
        openWorkbook(xlfile);
        sheet = workbook.getSheet(xlsheet);

        if (sheet.getRow(rownum) == null) {
            sheet.createRow(rownum);
        }

        row = sheet.getRow(rownum);
        cell = row.createCell(colnum);
        cell.setCellValue(data);
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cell.setCellStyle(style);

        saveAndClose(xlfile);
    }

    // Fill cell with red color
    public static void fillRedColor(String xlfile, String xlsheet, int rownum, int colnum, String data) throws IOException {
        openWorkbook(xlfile);
        sheet = workbook.getSheet(xlsheet);

        if (sheet.getRow(rownum) == null) {
            sheet.createRow(rownum);
        }

        row = sheet.getRow(rownum);
        cell = row.createCell(colnum);
        cell.setCellValue(data);

        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.RED.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cell.setCellStyle(style);

        saveAndClose(xlfile);
    }
}

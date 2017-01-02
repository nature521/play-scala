package com.kunpeng.detr;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ExcelDealer {

    public static byte[] ExcelDeal(InputStream excelStream) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook(excelStream);
        HSSFSheet sheet = workbook.getSheetAt(0);
        HSSFRow row = sheet.getRow(0);
        double L = row.getCell(0).getNumericCellValue();
        double R = row.getCell(1).getNumericCellValue();
        row.createCell(2).setCellValue(L + R);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        return out.toByteArray();
    }
}

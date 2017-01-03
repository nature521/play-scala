package com.kunpeng.detr;

import com.kunpeng.detr.EtermClient.DetrResultParser;
import com.kunpeng.detr.EtermClient.EtermClient;
import com.kunpeng.detr.EtermClient.EtermClientUtils;
import com.kunpeng.detr.EtermClient.PendingResult;
import com.kunpeng.detr.entity.DetrResult;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.functions.Now;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class ExcelDealer {
    private static EtermClient etermClient;
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelDealer.class);
    public static byte[] ExcelDeal(InputStream excelStream) throws IOException {
        String sendCmd, socketResult = null;
        PendingResult pendingResult = PendingResult.OTHER;
        while(etermClient == null){
            try{
                etermClient = EtermClientUtils.getEtermClient();
            }catch (Exception e){
                LOGGER.error("连接异常：{}", e);
            }
        }
        HSSFWorkbook workbook = new HSSFWorkbook(excelStream);
        HSSFSheet sheet = workbook.getSheetAt(0);
        int lastRowNum = sheet.getLastRowNum();
        String airCode = "0.0";
        Long ticketNum;
        for(int i = 1; i <= lastRowNum; i++){
            HSSFRow row = sheet.getRow(i);
            airCode = Double.toString(row.getCell(0).getNumericCellValue());
            ticketNum = Math.round(row.getCell(1).getNumericCellValue());
            sendCmd  = "detr" + " " + "TN" + airCode.substring(0,airCode.length() - 2) + "-" + ticketNum;
            try {
                socketResult = etermClient.SendRawCmd(sendCmd);
            } catch (Exception e) {
                e.printStackTrace();
            }
            List<String> regulationList = DetrResultParser.Regulation(socketResult);
            //结果；
            DetrResult detrResult = DetrResultParser.parse(regulationList);
            detrResult.createTime = DateTime.now().toDate();
            row.createCell(2).setCellValue(detrResult.airCompany);
            row.createCell(3).setCellValue(detrResult.flightNum);
            row.createCell(4).setCellValue(detrResult.cabin);
            row.createCell(5).setCellValue(detrResult.ticketStatus);
            row.createCell(6).setCellValue(detrResult.departureDate);
            row.createCell(7).setCellValue(detrResult.airRange);
            row.createCell(8).setCellValue(detrResult.ticketPrice);
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        return out.toByteArray();
    }
}

package com.kunpeng.detr;

import com.google.common.collect.Lists;
import com.kunpeng.detr.EtermClient.DetrResultParser;
import com.kunpeng.detr.EtermClient.EtermClient;
import com.kunpeng.detr.EtermClient.EtermClientUtils;
import com.kunpeng.detr.EtermClient.PendingResult;
import com.kunpeng.detr.Utils.StringUtil;
import com.kunpeng.detr.entity.DetrResultJV;
import com.kunpeng.detr.model.ByteAndDetr;
import com.kunpeng.detr.model.EtermConfig;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
    public static ByteAndDetr ExcelDeal(InputStream excelStream, String config) throws IOException {
        ByteAndDetr byteAndDetr = new ByteAndDetr();
        EtermConfig etermConfig = StringUtil.parseConfig(config);
        List<DetrResultJV> detrResultList = Lists.newArrayList();
        List<Object> res = Lists.newArrayList();
        String sendCmd, socketResult = null;
        PendingResult pendingResult = PendingResult.OTHER;
        while(etermClient == null){
            try{
                etermClient = EtermClientUtils.getEtermClient(etermConfig);
            }catch (Exception e){
                LOGGER.error("连接异常：{}", e);
            }
        }
        HSSFWorkbook workbook = new HSSFWorkbook(excelStream);
        HSSFSheet sheet = workbook.getSheetAt(0);
        int lastRowNum = sheet.getLastRowNum();
        String airCode = "0.0";
        Long ticketNum;
        setHead(sheet);
        for(int i = 1; i <= lastRowNum; i++){
            HSSFRow row = sheet.getRow(i);
            if(row.getCell(0) == null || row.getCell(1) == null
                    || row.getCell(0).toString().trim().isEmpty() || row.getCell(1).toString().trim().isEmpty()){
                continue;
            }
            airCode = Double.toString(row.getCell(0).getNumericCellValue());

            ticketNum = Math.round(row.getCell(1).getNumericCellValue());

            sendCmd  = "detr" + " " + "TN" + " "+ airCode.substring(0,airCode.length() - 2) + "-" + ticketNum;
            try {
                socketResult = etermClient.SendRawCmd(sendCmd);
                //有时候网络不好，需要重发，
                int count = 1;
                while(socketResult == null || socketResult.equals("欢迎使用哈尔滨中心软件ETerm业务系统")){
                    socketResult = etermClient.SendRawCmd(sendCmd);
                    LOGGER.info("重发次数：{}", count ++);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            List<String> regulationList = DetrResultParser.Regulation(socketResult);
            //结果；
            DetrResultJV detrResult = DetrResultParser.parse(regulationList);
            if(detrResult == null ||detrResult.airCode == null || detrResult.ticketNum == null){
                continue;
            }
            detrResult.createTime = DateTime.now().toDate();
            row.createCell(2).setCellValue(detrResult.airCompany);
            row.createCell(3).setCellValue(detrResult.flightNum);
            row.createCell(4).setCellValue(detrResult.cabin);
            row.createCell(5).setCellValue(detrResult.ticketStatus);
            row.createCell(6).setCellValue(detrResult.departureDate);
            row.createCell(7).setCellValue(detrResult.airRange);
            row.createCell(8).setCellValue(detrResult.ticketPrice);
            row.createCell(9).setCellValue(new DateTime(detrResult.departureDate2).toString("yyyy-MM-dd"));
            detrResultList.add(detrResult);
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        byteAndDetr.setBytes(out.toByteArray());
        byteAndDetr.setDetrResultList(detrResultList);
        etermClient = null;
        return byteAndDetr;
    }


    public static void setHead(HSSFSheet sheet){
        HSSFRow row = sheet.getRow(0);
        row.createCell(2).setCellValue("航空公司");
        row.createCell(3).setCellValue("航班号");
        row.createCell(4).setCellValue("舱位");
        row.createCell(5).setCellValue("机票状态");
        row.createCell(6).setCellValue("乘机日期");
        row.createCell(7).setCellValue("航程");
        row.createCell(8).setCellValue("票面价");
        row.createCell(9).setCellValue("乘机日期2");
    }

}

package com.kunpeng.detr.EtermClient;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.kunpeng.detr.entity.DetrResult;

import java.io.*;
import java.util.List;
import java.util.Properties;

/**
 * Created by Administrator on 2016/4/14.
 */
public class DetrResultParser {
    private static String JAVA_SEPERATOR = "\n";
    public static List<String> Regulation(String src) {

        //1.将\r\n空格替换成\r\n\r
        //这个里边出现比较整齐，先不替换；
        //String temp = src.replace("\r\n ", "\r\n\r");
        String temp = src.replace("\t", "");

        //2.对报文进行\r\n\r分割
        List<String> lines = Splitter.on(JAVA_SEPERATOR).trimResults().splitToList(temp);
        //List<String> lines = temp.split(SEPERATOR, StringSplitOptions.RemoveEmptyEntries).ToList();

        //3.去掉**开头的提示, 去掉<>及之后的元素
        List<String> regularList = Lists.newArrayList();

        for (String line : lines) {
            //3.1 过滤掉以**开头的
            if(line.contains("ISSUED") || line.contains("E/R") || line.contains("TOUR CODE") || line.contains("PASSENGER")
                    || line.contains("EXCH") || line.contains("TAX") || line.contains("TOTAL")){
                continue;
            }
            //3.2找到左括号的位置
            int index = line.indexOf('<');
            regularList.add(line);
        }

        return regularList;
    }

    public static DetrResult parse(List<String> regList){
        DetrResult detrResult = new DetrResult();
        String goAirCompany = "";
        String returnAirCompany = "";
        String goFlightNum = "";
        String returnFlightNum = "";
        String goCanin = "";
        String returnCabin = "";
        String goDepartureDate = "";
        String returnDepartureDate = "";
        String goStatus = "";
        String returnStatus = "";
        String goDeaprtureCity = "";
        String goArriveCity = "";
        String returnArriveCity = "";
        for(String reg : regList){
            if(reg.startsWith("DETR")){
                detrResult.airCode = reg.substring(7,10);
                detrResult.ticketNum = Long.valueOf(reg.substring(11,21));
            }else if(reg.startsWith("O FM")){
                goAirCompany  = reg.substring(10,12);
                goFlightNum = reg.substring(16,20);
                goCanin = reg.substring(22,23);
                goDepartureDate = reg.substring(24,29);
                goStatus = reg.substring(63);
                goDeaprtureCity = reg.substring(6,9);
            }else if(reg.startsWith("O TO")){
                returnAirCompany = reg.substring(10,12);
                returnFlightNum = reg.substring(16,20);
                returnCabin = reg.substring(22,23);
                returnDepartureDate = reg.substring(24,29);
                returnStatus = reg.substring(63);
                goArriveCity = reg.substring(6,9);
            }else if(reg.startsWith("FARE")){
                int indexEnd = reg.indexOf("|");
                detrResult.ticketPrice = Integer.valueOf(reg.substring(20, indexEnd - 3).trim());
            }else if(reg.startsWith("TO")){
                returnArriveCity = reg.substring(4,7);
            }
        }
        detrResult.airCompany = goAirCompany;
        if(!returnAirCompany.equals("") && (!goAirCompany.equals(returnAirCompany))){
            detrResult.airCompany = goAirCompany + "//" + returnAirCompany;
        }
        detrResult.flightNum = goFlightNum;
        if(!returnFlightNum.equals("")){
            detrResult.flightNum = goFlightNum + "//" + returnFlightNum;
        }
        detrResult.cabin = goCanin;
        if(!returnCabin.equals("")){
            detrResult.cabin = goCanin + "//" + returnCabin;
        }
        detrResult.departureDate = goDepartureDate;
        if(!returnDepartureDate.equals("")){
            detrResult.departureDate = goDepartureDate + "//"  + returnDepartureDate;
        }
        detrResult.ticketStatus = goStatus;
        if(!returnStatus.equals("")){
            detrResult.ticketStatus = goStatus + "//" + returnStatus;
        }
        if(Strings.isNullOrEmpty(goArriveCity))
        {
            detrResult.airRange = goDeaprtureCity + "-" + returnArriveCity;
        }else{
            detrResult.airRange = goDeaprtureCity + "-" + goArriveCity + "//" + goArriveCity  + "-" + returnArriveCity;
        }

        return detrResult;
    }


}
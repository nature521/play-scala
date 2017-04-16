package com.kunpeng.detr.EtermClient;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.kunpeng.detr.Utils.DateUtil;
import com.kunpeng.detr.entity.DetrResultJV;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Administrator on 2016/4/14.
 */
public class DetrResultParser {
    private static Logger LOGGER = LoggerFactory.getLogger(DetrResultParser.class);
    private static String JAVA_SEPERATOR = "\n";
    public static List<String> Regulation(String src) {
        LOGGER.info("报文：{}", src);
        //1.对报文进行\r\n\r分割
        List<String> lines = Splitter.on(JAVA_SEPERATOR).trimResults().splitToList(src);
        List<String> regularList = Lists.newArrayList();
        if(src.contains("ENSURE XMIT MODE IS VAR AND USE REF: TO REFRESH SCREEN")
           || src.contains("ET PASSENGER DATA NOT FOUND") || src.contains("指令未授")
           || src.contains("ET TICKET NUMBER IS NOT EXIST")){
            return regularList;
        }
        for (String line : lines) {
            //2.去掉没用的信息；
            if(line.contains("ISSUED") || line.contains("E/R") || line.contains("TOUR CODE") || line.contains("PASSENGER")
                    || line.contains("EXCH") || line.contains("TAX") || line.contains("TOTAL")){
                continue;
            }
            regularList.add(line);
        }
        return regularList;
    }

    public static DetrResultJV parse(List<String> regList){

        if(regList.isEmpty()){
            return null;
        }
        DetrResultJV detrResult = new DetrResultJV();
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
                //goFlightNum = goAirCompany + reg.substring(16,20);
                goCanin = reg.substring(22,23);
               // goDepartureDate = reg.substring(24,29);
                goStatus = reg.substring(67);
                goDeaprtureCity = reg.substring(6,9);
            }else if(reg.startsWith("O TO")){
                returnAirCompany = reg.substring(10,12);
                returnFlightNum = returnAirCompany + reg.substring(16,20);
                returnCabin = reg.substring(22,23);
                //returnDepartureDate = reg.substring(24,29);
                returnStatus = reg.substring(67);
                goArriveCity = reg.substring(6,9);
            }else if(reg.startsWith("FARE") ){
                int indexEnd = reg.indexOf("|");
                if(reg.substring(20, indexEnd - 3).trim().equals("")){
                    detrResult.ticketPrice = 0;
                }else{
                    detrResult.ticketPrice = Integer.valueOf(reg.substring(20, indexEnd - 3).trim());
                }

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
            //detrResult.flightNum = goFlightNum + "//" + returnFlightNum;
        }
        detrResult.cabin = goCanin;
        if(!returnCabin.equals("")){
            detrResult.cabin = goCanin + "//" + returnCabin;
        }
        detrResult.departureDate = goDepartureDate;
        if(!returnDepartureDate.equals("")){
           // detrResult.departureDate = goDepartureDate + "//"  + returnDepartureDate;
        }

        //departureDate2只写第一段乘机日期；
        //Boolean isBeforeNow = true;
        Boolean isBeforeNow = isBeforeNow(goStatus);
        //detrResult.departureDate2 = DateUtil.DateCovCh(goDepartureDate, isBeforeNow);
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

    /*public static DetrResultJV parse(List<String> regList){

        if(regList.isEmpty()){
            return null;
        }
        DetrResultJV detrResult = new DetrResultJV();
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
                goFlightNum = goAirCompany + reg.substring(16,20);
                goCanin = reg.substring(22,23);
                goDepartureDate = reg.substring(24,29);
                goStatus = reg.substring(63);
                goDeaprtureCity = reg.substring(6,9);
            }else if(reg.startsWith("O TO")){
                returnAirCompany = reg.substring(10,12);
                returnFlightNum = returnAirCompany + reg.substring(16,20);
                returnCabin = reg.substring(22,23);
                returnDepartureDate = reg.substring(24,29);
                returnStatus = reg.substring(63);
                goArriveCity = reg.substring(6,9);
            }else if(reg.startsWith("FARE") ){
                int indexEnd = reg.indexOf("|");
                if(reg.substring(20, indexEnd - 3).trim().equals("")){
                    detrResult.ticketPrice = 0;
                }else{
                    detrResult.ticketPrice = Integer.valueOf(reg.substring(20, indexEnd - 3).trim());
                }

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

        //departureDate2只写第一段乘机日期；
        //Boolean isBeforeNow = true;
        Boolean isBeforeNow = isBeforeNow(goStatus);
        detrResult.departureDate2 = DateUtil.DateCovCh(goDepartureDate, isBeforeNow);
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
    }*/

    //已经用过的说明比现在早；
    public static Boolean isBeforeNow(String goStatus){

        if(goStatus.contains("USED/FLOWN")){
            return true;
        }else if(goStatus.contains("OPEN FOR USE")){
            return false;
        }
        return false;

    }
}
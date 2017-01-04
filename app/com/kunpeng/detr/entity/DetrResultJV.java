package com.kunpeng.detr.entity;

import java.util.Date;

/**
 * Created by Administrator on 2016/12/29.
 */
public class DetrResultJV {
    public Long id;
    public Long ticketNum;
    public String ticketStatus;
    public String airCompany;
    public String flightNum;
    public String cabin;
    public String departureDate;
    public int ticketPrice;
    public String airRange;
    public String keyCount;
    public int useStatus;
    public String airCode;
    public Date departureDate2;
    public Date createTime;
    public String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTicketNum() {
        return ticketNum;
    }

    public void setTicketNum(Long ticketNum) {
        this.ticketNum = ticketNum;
    }

    public String getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(String ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public String getAirCompany() {
        return airCompany;
    }

    public void setAirCompany(String airCompany) {
        this.airCompany = airCompany;
    }

    public String getFlightNum() {
        return flightNum;
    }

    public void setFlightNum(String flightNum) {
        this.flightNum = flightNum;
    }

    public String getCabin() {
        return cabin;
    }

    public void setCabin(String cabin) {
        this.cabin = cabin;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public int getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(int ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public String getAirRange() {
        return airRange;
    }

    public void setAirRange(String airRange) {
        this.airRange = airRange;
    }

    public String getKeyCount() {
        return keyCount;
    }

    public void setKeyCount(String keyCount) {
        this.keyCount = keyCount;
    }

    public int getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(int useStatus) {
        this.useStatus = useStatus;
    }

    public String getAirCode() {
        return airCode;
    }

    public void setAirCode(String airCode) {
        this.airCode = airCode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getDepartureDate2() {
        return departureDate2;
    }

    public void setDepartureDate2(Date departureDate2) {
        this.departureDate2 = departureDate2;
    }
}

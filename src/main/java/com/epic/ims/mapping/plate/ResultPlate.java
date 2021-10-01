package com.epic.ims.mapping.plate;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope("prototype")
public class ResultPlate {
    String barcode;
    String resultId;
    String ct1;
    String ct2;
    String plateid;
    String receivedDate;
    String remark;
    String blockValue;

    public ResultPlate(){
    }

    public ResultPlate(String barcode, String resultId, String ct1, String ct2, String plateid, String receivedDate, String remark, String blockValue) {
        this.barcode = barcode;
        this.resultId = resultId;
        this.ct1 = ct1;
        this.ct2 = ct2;
        this.plateid = plateid;
        this.receivedDate = receivedDate;
        this.remark = remark;
        this.blockValue = blockValue;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getResultId() {
        return resultId;
    }

    public void setResultId(String resultId) {
        this.resultId = resultId;
    }

    public String getCt1() {
        return ct1;
    }

    public void setCt1(String ct1) {
        this.ct1 = ct1;
    }

    public String getCt2() {
        return ct2;
    }

    public void setCt2(String ct2) {
        this.ct2 = ct2;
    }

    public String getPlateid() {
        return plateid;
    }

    public void setPlateid(String plateid) {
        this.plateid = plateid;
    }

    public String getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(String receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBlockValue() {
        return blockValue;
    }

    public void setBlockValue(String blockValue) {
        this.blockValue = blockValue;
    }
}

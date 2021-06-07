package com.epic.ims.bean.samplefileupload;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SampleData {
    private String referenceNo; //---------------> mandatory
    private String date;
    private String mohArea;//--------------------> mandatory -----------> special characters
    private String name;//-----------------------> mandatory -----------> special characters
    private String age;//------------------------> special characters
    private String gender;//---------------------> special characters
    private String symptomatic;
    private String contactType;
    private String nic;//------------------------> mandatory
    private String address;
    private String residentDistrict;
    private String contactNumber;//--------------> mandatory
    private String secondaryContactNumber;

    @Override
    public int hashCode() {
        return referenceNo.hashCode() ^ mohArea.hashCode() ^ date.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SampleData) {
            SampleData sampleData = (SampleData) obj;
            return sampleData.referenceNo.equals(referenceNo) && sampleData.mohArea.equals(mohArea) && sampleData.date.equals(date);
        }
        return false;
    }
}

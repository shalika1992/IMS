package com.epic.ims.bean.resultupdate;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RepeatSampleBean {
    private String referenceNo; //---------------> mandatory
    private String date;
    private String mohArea;//--------------------> mandatory
    private String name;//-----------------------> mandatory
    private String age;
    private String gender;
    private String symptomatic;
    private String contactType;
    private String nic;//------------------------> mandatory
    private String address;
    private String residentDistrict;
    private String contactNumber;//--------------> mandatory
    private String secondaryContactNumber;
    private String receiveddate;

}
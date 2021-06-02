package com.epic.ims.bean.plate;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ResultBean {
    private String[] id;
    private String[] referenceNo;
    private String[] name;
    private String[] nic;
    private String barcode;
    private String plateid;
    private String blockvalue;
    private String ispool;
    private String iscomplete;
    private String result;
}

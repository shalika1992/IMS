package com.epic.ims.bean.plate;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class DefaultBean {
    private String[] id;
    private String[] referenceNo;
    private String[] name;
    private String[] nic;
    private String labcode;
    private String plateid;
    private String blockvalue;
    private String ispool;
}

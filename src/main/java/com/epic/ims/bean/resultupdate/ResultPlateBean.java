package com.epic.ims.bean.resultupdate;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ResultPlateBean {
    String barcode;
    String resultId;
    String ct1;
    String ct2;
    String plateid;
    String receivedDate;
    String remark;
    String blockValue;
}

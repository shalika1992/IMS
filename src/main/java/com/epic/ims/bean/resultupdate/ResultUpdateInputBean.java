package com.epic.ims.bean.resultupdate;

import com.epic.ims.mapping.institution.Institution;
import com.epic.ims.mapping.plate.Plate;
import com.epic.ims.util.common.DataTablesRequest;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ResultUpdateInputBean extends DataTablesRequest {
    private String receivedDate;
    private String referenceNo;
    private String name;
    private String nic;
    private String institutionCode;
    private String plateId;
    private List<Institution> institutionList;
    private List<Plate> plateList;
}

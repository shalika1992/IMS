package com.epic.ims.mapper.mastertemp;

import com.epic.ims.mapping.mastertemp.MasterTemp;
import com.epic.ims.mapping.samplefile.SampleFile;
import com.epic.ims.util.common.Common;
import com.epic.ims.util.varlist.CommonVarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Scope("prototype")
public class MasterTempDataMapper {

    @Autowired
    CommonVarList commonVarList;

    @Autowired
    Common common;

    public List<MasterTemp> sampleListMasterTempList(List<SampleFile> sampleFileList) {
        List<MasterTemp> masterTempList = null;
        try {
            if (sampleFileList != null && !sampleFileList.isEmpty() && sampleFileList.size() > 0) {
                masterTempList = sampleFileList.stream().map(s -> this.SampleToMasterTemp(s)).collect(Collectors.toList());
            }
        } catch (Exception e) {
            throw e;
        }
        return masterTempList;
    }

    private MasterTemp SampleToMasterTemp(SampleFile s) {
        MasterTemp masterTemp = new MasterTemp();
        try {
            masterTemp.setSampleId(s.getId());
            masterTemp.setReferenceNo(s.getReferenceNo());
            masterTemp.setInstitutionCode(s.getInstitutionCode());
            masterTemp.setName(s.getName());
            masterTemp.setAge(s.getAge());
            masterTemp.setGender(s.getGender());
            masterTemp.setNic(s.getNic());
            masterTemp.setSymptomatic(s.getSymptomatic());
            masterTemp.setContactType(s.getContactType());
            masterTemp.setAddress(s.getAddress());
            masterTemp.setResidentDistrict(s.getResidentDistrict());
            masterTemp.setContactNumber(s.getContactNumber());
            masterTemp.setSecondaryContactNumber(s.getSecondaryContactNumber());
            masterTemp.setBarcode(s.getBarcode());
            masterTemp.setReceivedDate(s.getReceivedDate());
            masterTemp.setStatus(s.getStatus());
            masterTemp.setWard(s.getWard());
            masterTemp.setPlateId(s.getPlateId());
            masterTemp.setBlockValue(s.getBlockValue());
            masterTemp.setCollectionDate(s.getCollectionDate());
        } catch (Exception e) {
            throw e;
        }
        return masterTemp;
    }
}

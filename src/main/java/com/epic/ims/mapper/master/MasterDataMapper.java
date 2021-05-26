package com.epic.ims.mapper.master;

import com.epic.ims.util.common.Common;
import com.epic.ims.util.varlist.CommonVarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class MasterDataMapper {

    @Autowired
    CommonVarList commonVarList;

    @Autowired
    Common common;


}

package com.epic.ims.controller.resultupdate;

import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.controller.samplefileupload.SampleFileUploadController;
import com.epic.ims.util.varlist.CommonVarList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("request")
public class ResultUpdateController {
    private static Logger logger = LogManager.getLogger(SampleFileUploadController.class);

    @Autowired
    SessionBean sessionBean;

    @Autowired
    MessageSource messageSource;

    @Autowired
    CommonVarList commonVarList;
}

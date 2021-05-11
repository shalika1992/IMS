package com.epic.ims.repository.reportmgt;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository
@Scope("prototype")
public class ReportMgtRepository {
    private static Logger logger = LogManager.getLogger(ReportMgtRepository.class);

}

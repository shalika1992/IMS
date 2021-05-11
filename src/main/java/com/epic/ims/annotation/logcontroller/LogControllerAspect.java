package com.epic.ims.annotation.logcontroller;

import com.epic.ims.util.common.Common;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogControllerAspect {
    private static Logger logger = LogManager.getLogger(LogControllerAspect.class);

    @Autowired
    Common common;

    @Before(value = "@annotation(LogController) && args(object)")
    public void beforeRestApiLog(JoinPoint joinPoint, Object object) {
        common.writeLog(joinPoint, object);
    }

    @AfterReturning(value = "@annotation(LogController)", returning = "object")
    public void afterRestApiLog(JoinPoint joinPoint, Object object) {
        common.writeLog(joinPoint, object);
    }
}

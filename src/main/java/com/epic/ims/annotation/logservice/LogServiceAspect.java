package com.epic.ims.annotation.logservice;

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
public class LogServiceAspect {
    private static Logger logger = LogManager.getLogger(LogServiceAspect.class);

    @Autowired
    Common common;

    @Before(value = "@annotation(LogService) && args(object,..)")
    public void beforeServiceLog(JoinPoint joinPoint, Object object) {
        common.writeLog(joinPoint, object);
    }

    @AfterReturning(value = "@annotation(LogService)", returning = "object")
    public void afterServiceLog(JoinPoint joinPoint, Object object) {
        common.writeLog(joinPoint, object);
    }
}

package com.epic.ims.annotation.logrespository;

import com.epic.ims.util.common.Common;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogRepositoryAspect {
    private static Logger logger = LogManager.getLogger(LogRepositoryAspect.class);

    @Autowired
    Common common;

    @AfterReturning(pointcut = "within(@org.springframework.stereotype.Repository *)", returning = "object")
    public void logAfterReturning(JoinPoint joinPoint, Object object) {
        common.writeLog(joinPoint, object);
    }
}

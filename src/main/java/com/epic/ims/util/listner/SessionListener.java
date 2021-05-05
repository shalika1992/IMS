package com.epic.ims.util.listner;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionListener implements HttpSessionListener {
    private final Log logger = LogFactory.getLog(getClass());

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        logger.info("[" + se.getSession().getId() + "]  New Session created ");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        logger.info("[" + se.getSession().getId() + "]  Session destroyed ");
    }
}

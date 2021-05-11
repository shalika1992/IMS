package com.epic.ims.util.listner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionListener implements HttpSessionListener {
    private static Logger logger = LogManager.getLogger(SessionListener.class);

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        logger.info("[" + se.getSession().getId() + "]  New Session created ");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        logger.info("[" + se.getSession().getId() + "]  Session destroyed ");
    }
}

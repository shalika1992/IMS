package com.epic.ims.util.interceptor;

import com.epic.ims.bean.session.SessionBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AuthInterceptor implements HandlerInterceptor {
    private final Log logger = LogFactory.getLog(getClass());

    @Resource
    private SessionBean sessionBean;

    @Autowired
    ServletContext servletContext;

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean status = false;
        //get method and class name details
        HandlerMethod method = (HandlerMethod) handler;
        Class<?> className = method.getMethod().getDeclaringClass();
        String methodName = method.getMethod().getName();
        // get the http session
        RequestDispatcher requestDispatcher;
        HttpSession httpSession = request.getSession(false);
        try {
            // set standard http/1.1 no-cache headers.
            response.setHeader("Cache-Control", "private, no-store, no-cache, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setHeader("X-Content-Type-Options", "nosniff");

            status = true;
        } catch (Exception e) {
            logger.error("Interceptor perHandle Exception : ", e);
            if (!methodName.trim().equalsIgnoreCase("checkuser") && !methodName.trim().equalsIgnoreCase("logout")) {
                response.sendRedirect("logout.htm");
            } else {
                status = true;
            }
        }
        return status;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        try {
            if (sessionBean != null && sessionBean.getUser() != null) {
                logger.info("--Interceptor post handle -- ");
            }
        } catch (Exception e) {
            logger.error("Interceptor postHandle Exception : ", e);
        } finally {
            try {
                response.addHeader("X-FRAME-OPTIONS", "DENY");
            } catch (Exception e) {
                logger.error("Interceptor postHandle Exception : ", e);
                throw e;
            }
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}

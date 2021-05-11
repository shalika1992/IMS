package com.epic.ims.util.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter(urlPatterns = {"/*"})
public class HttpRequestFilter implements Filter {
    private static Logger logger = LogManager.getLogger(HttpRequestFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("---HttpRequestFilter initialized---");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        System.out.println("---HttpRequestFilter destroyed---");
    }
}

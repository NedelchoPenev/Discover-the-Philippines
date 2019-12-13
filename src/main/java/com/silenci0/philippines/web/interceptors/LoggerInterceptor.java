package com.silenci0.philippines.web.interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

public class LoggerInterceptor extends HandlerInterceptorAdapter {

  private static final Logger logger = LoggerFactory
    .getLogger(LoggerInterceptor.class);

  @Override
  public boolean preHandle(
    HttpServletRequest request,
    HttpServletResponse response,
    Object handler) {

    logger.info("[preHandle][" + request + "]" + "[" + request.getMethod()
      + "] \n\r" + "[url: " + request.getRequestURI() + "] " + getParameters(request));

    return true;
  }

  @Override
  public void postHandle(
    HttpServletRequest request,
    HttpServletResponse response,
    Object handler,
    ModelAndView modelAndView) {

    logger.info("[postHandle][" + request + "]");
  }

  @Override
  public void afterCompletion(
    HttpServletRequest request, HttpServletResponse response,Object handler, Exception ex) {
    if (ex != null){
      ex.printStackTrace();
    }
    logger.info("[afterCompletion][" + request + "][exception: " + ex + "]");
  }

  private String getParameters(HttpServletRequest request) {
    StringBuffer posted = new StringBuffer();
    Enumeration<?> e = request.getParameterNames();
    if (e != null) {
      posted.append("?");
    }
    while (e.hasMoreElements()) {
      if (posted.length() > 1) {
        posted.append("&");
      }
      String curr = (String) e.nextElement();
      posted.append(curr + "=");
      if (curr.contains("password")
        || curr.contains("pass")
        || curr.contains("pwd")) {
        posted.append("*****");
      } else {
        posted.append(request.getParameter(curr));
      }
    }
    String ip = request.getHeader("X-FORWARDED-FOR");
    String ipAddr = (ip == null) ? getRemoteAddr(request) : ip;
    if (ipAddr!=null && !ipAddr.equals("")) {
      posted.append("&_psip=" + ipAddr);
    }
    return posted.toString();
  }

  private String getRemoteAddr(HttpServletRequest request) {
    String ipFromHeader = request.getHeader("X-FORWARDED-FOR");
    if (ipFromHeader != null && ipFromHeader.length() > 0) {
      logger.debug("ip from proxy - X-FORWARDED-FOR : " + ipFromHeader);
      return ipFromHeader;
    }
    return request.getRemoteAddr();
  }
}

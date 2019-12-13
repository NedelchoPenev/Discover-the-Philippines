package com.silenci0.philippines.web.interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestProcessingTimeInterceptor extends HandlerInterceptorAdapter {

  private static final Logger logger = LoggerFactory
    .getLogger(RequestProcessingTimeInterceptor.class);

  @Override
  public boolean preHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler) {
    long startTime = System.currentTimeMillis();
    logger.info("Request URL::" + request.getRequestURL().toString()
      + ":: Start Time=" + System.currentTimeMillis());
    request.setAttribute("startTime", startTime);
    return true;
  }

  @Override
  public void postHandle(HttpServletRequest request,
                         HttpServletResponse response, Object handler,
                         ModelAndView modelAndView) {
    System.out.println("Request URL::" + request.getRequestURL().toString()
      + " Sent to Handler :: Current Time=" + System.currentTimeMillis());
  }

  @Override
  public void afterCompletion(HttpServletRequest request,
                              HttpServletResponse response, Object handler, Exception ex) {
    long startTime = (Long) request.getAttribute("startTime");
    logger.info("Request URL::" + request.getRequestURL().toString()
      + ":: End Time=" + System.currentTimeMillis());
    logger.info("Request URL::" + request.getRequestURL().toString()
      + ":: Time Taken=" + (System.currentTimeMillis() - startTime) + "ms");
  }

}
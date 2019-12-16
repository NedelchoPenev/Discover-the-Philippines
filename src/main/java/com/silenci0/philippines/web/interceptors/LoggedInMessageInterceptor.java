package com.silenci0.philippines.web.interceptors;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoggedInMessageInterceptor extends HandlerInterceptorAdapter {

  @Override
  public void postHandle(HttpServletRequest request,
                         HttpServletResponse response,
                         Object handler,
                         ModelAndView modelAndView)  {

    HttpSession session = request.getSession();

    if(session.getAttribute("username") != null){
      session.removeAttribute("username");
      modelAndView.addObject("showWelcomeMessage", true);
    }
  }
}

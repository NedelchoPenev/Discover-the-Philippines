package com.silenci0.philippines.web.controllers;

import org.springframework.web.servlet.ModelAndView;

public abstract class BaseController {

  protected ModelAndView view(String view, String objectName, Object object, ModelAndView modelAndView) {
    modelAndView.addObject("view", view);
    modelAndView.addObject(objectName, object);

    modelAndView.setViewName("fragments/base-layout");

    return modelAndView;
  }

  protected ModelAndView view(String view, ModelAndView modelAndView) {
    modelAndView.addObject("view", view);

    modelAndView.setViewName("fragments/base-layout");

    return modelAndView;
  }

  protected ModelAndView redirect(String url, ModelAndView modelAndView) {
    modelAndView.setViewName("redirect:" + url);

    return modelAndView;
  }

//    protected ModelAndView view(String view, ModelAndView modelAndView) {
//        modelAndView.setViewName(view);
//
//        return modelAndView;
//    }
//
//    protected ModelAndView view(String view) {
//        return this.view(view, new ModelAndView());
//    }
//
//    protected ModelAndView redirect(String url) {
//        return this.view("redirect:" + url);
//    }
}

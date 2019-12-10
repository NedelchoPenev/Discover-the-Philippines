//package com.silenci0.philippines.error;
//
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.multipart.MaxUploadSizeExceededException;
//import org.springframework.web.servlet.ModelAndView;
//
//@ControllerAdvice
//public class FileUploadExceptionAdvice {
//
//  @ExceptionHandler(MaxUploadSizeExceededException.class)
//  public ModelAndView handleMaxSizeException(
//    MaxUploadSizeExceededException exc) {
//
//    ModelAndView modelAndView = new ModelAndView("fragments/base-layout");
//
//    modelAndView.addObject("view", "error/image-size-error");
//
//    return modelAndView;
//  }
//}


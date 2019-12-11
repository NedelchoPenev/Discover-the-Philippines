package com.silenci0.philippines.error;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

@ControllerAdvice
public class CustomGlobalExceptionHandler {

  @ExceptionHandler({IllegalArgumentException.class, UsernameNotFoundException.class})
  public void springHandleNotFound(HttpServletResponse response) throws IOException {
    response.sendError(HttpStatus.NOT_FOUND.value());
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ModelAndView handleAccessDeniedException(AccessDeniedException exc, Principal principal) {

    ModelAndView modelAndView = new ModelAndView("redirect:" + "/");
    if (principal == null){
      modelAndView.addObject("showLoginModal", true);
    }

    return modelAndView;
  }

  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ModelAndView handleMaxSizeException(
    MaxUploadSizeExceededException exc) {

    ModelAndView modelAndView = new ModelAndView("fragments/base-layout");

    modelAndView.addObject("view", "error/image-size-error");

    return modelAndView;
  }
}

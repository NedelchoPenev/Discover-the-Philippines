package com.silenci0.philippines.web.controllers;

import com.silenci0.philippines.domain.models.binding.RegisterUserBindingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/error")
public class CustomErrorController extends AbstractErrorController {
  private final ErrorProperties errorProperties;

  public CustomErrorController(ErrorAttributes errorAttributes, ErrorProperties errorProperties) {
    this(errorAttributes, errorProperties, Collections.emptyList());
  }

  @Autowired
  public CustomErrorController(ErrorAttributes errorAttributes, ErrorProperties errorProperties, List<ErrorViewResolver> errorViewResolvers) {
    super(errorAttributes, errorViewResolvers);
    Assert.notNull(errorProperties, "ErrorProperties must not be null");
    this.errorProperties = errorProperties;
  }

  public String getErrorPath() {
    return this.errorProperties.getPath();
  }

  @RequestMapping(
    produces = {"text/html"}
  )
  public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response,
                                @ModelAttribute("bindingModel")
                                  RegisterUserBindingModel bindingModel) {
    HttpStatus status = this.getStatus(request);
    Map<String, Object> model = Collections.unmodifiableMap(this.getErrorAttributes(request, this.isIncludeStackTrace(request, MediaType.TEXT_HTML)));
    response.setStatus(status.value());
    ModelAndView modelAndView = this.resolveErrorView(request, response, status, model);
    return modelAndView != null ? modelAndView : new ModelAndView("error", model);
  }

  @RequestMapping
  public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
    Map<String, Object> body = this.getErrorAttributes(request, this.isIncludeStackTrace(request, MediaType.ALL));
    HttpStatus status = this.getStatus(request);
    return new ResponseEntity(body, status);
  }

  protected boolean isIncludeStackTrace(HttpServletRequest request, MediaType produces) {
    ErrorProperties.IncludeStacktrace include = this.getErrorProperties().getIncludeStacktrace();
    if (include == ErrorProperties.IncludeStacktrace.ALWAYS) {
      return true;
    } else {
      return include == ErrorProperties.IncludeStacktrace.ON_TRACE_PARAM ? this.getTraceParameter(request) : false;
    }
  }

  protected ErrorProperties getErrorProperties() {
    return this.errorProperties;
  }
}

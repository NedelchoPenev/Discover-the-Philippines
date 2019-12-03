package com.silenci0.philippines.web.controllers;

import com.silenci0.philippines.domain.models.binding.RegisterUserBindingModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/contact")
public class ContactController extends BaseController{

    @GetMapping("")
    public ModelAndView getContact(@ModelAttribute("bindingModel") RegisterUserBindingModel bindingModel, ModelAndView modelAndView) {
        return this.view("other/contact", "pageName", "Contact us", modelAndView);
    }
}

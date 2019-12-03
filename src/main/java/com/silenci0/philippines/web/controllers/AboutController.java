package com.silenci0.philippines.web.controllers;

import com.silenci0.philippines.domain.models.binding.RegisterUserBindingModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/about")
public class AboutController extends BaseController{

    @GetMapping("")
    public ModelAndView getAbout(@ModelAttribute("bindingModel") RegisterUserBindingModel bindingModel, ModelAndView modelAndView) {
        return this.view("other/about", "pageName", "About", modelAndView);
    }
}

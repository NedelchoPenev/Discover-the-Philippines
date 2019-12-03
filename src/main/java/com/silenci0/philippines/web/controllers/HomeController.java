package com.silenci0.philippines.web.controllers;

import com.silenci0.philippines.domain.models.binding.RegisterUserBindingModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping
public class HomeController extends BaseController {
    @GetMapping("")
    public ModelAndView index(@ModelAttribute("bindingModel") RegisterUserBindingModel bindingModel, ModelAndView modelAndView) {

        return view("index", modelAndView);
    }

    @GetMapping("/home")
    public ModelAndView home(@ModelAttribute("bindingModel") RegisterUserBindingModel bindingModel, ModelAndView modelAndView) {
        return redirect("/", modelAndView);
    }
}

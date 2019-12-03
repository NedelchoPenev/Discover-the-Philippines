package com.silenci0.philippines.web.controllers;

import com.silenci0.philippines.domain.models.binding.RegisterUserBindingModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/blog")
public class BlogController extends BaseController{

    @GetMapping("")
    public ModelAndView getBlog(@ModelAttribute("bindingModel") RegisterUserBindingModel bindingModel, ModelAndView modelAndView) {
        return this.view("blog/blog", modelAndView);
    }

    @GetMapping("single")
    public ModelAndView getSingleBlog(@ModelAttribute("bindingModel") RegisterUserBindingModel bindingModel, ModelAndView modelAndView) {
        return this.view("blog/single-blog", modelAndView);
    }
}

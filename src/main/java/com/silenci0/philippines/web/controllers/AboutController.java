package com.silenci0.philippines.web.controllers;

import com.silenci0.philippines.domain.models.binding.RegisterUserBindingModel;
import com.silenci0.philippines.domain.models.view.UserAboutViewModel;
import com.silenci0.philippines.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/about")
public class AboutController extends BaseController{
    private final UserService userService;
    private final ModelMapper modelMapper;

    public AboutController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("")
    public ModelAndView getAbout(@ModelAttribute("bindingModel")
                                       RegisterUserBindingModel bindingModel,
                                 ModelAndView modelAndView) {

        List<UserAboutViewModel> team = this.userService.findCreators()
          .stream()
          .map(u -> this.modelMapper.map(u, UserAboutViewModel.class))
          .collect(Collectors.toList());

        modelAndView.addObject("team", team);

        return this.view("other/about", "pageName", "About", modelAndView);
    }
}

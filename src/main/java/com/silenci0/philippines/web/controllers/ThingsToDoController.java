package com.silenci0.philippines.web.controllers;

import com.silenci0.philippines.domain.models.binding.RegisterUserBindingModel;
import com.silenci0.philippines.domain.models.service.ThingsToDoMainImageServiceModel;
import com.silenci0.philippines.domain.models.service.ThingsToDoServiceModel;
import com.silenci0.philippines.domain.models.view.ThingsToDoDetailsViewModel;
import com.silenci0.philippines.domain.models.view.ThingsToDoViewModel;
import com.silenci0.philippines.service.ThingsToDoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/things-to-do")
public class ThingsToDoController extends BaseController {

  private final ThingsToDoService thingsToDoService;
  private ModelMapper modelMapper;

  @Autowired
  public ThingsToDoController(ThingsToDoService thingsToDoService, ModelMapper modelMapper) {
    this.thingsToDoService = thingsToDoService;
    this.modelMapper = modelMapper;
  }

  @GetMapping("")
  public ModelAndView getThingsToDo(@ModelAttribute("bindingModel")
                                      RegisterUserBindingModel bindingModel,
                                    ModelAndView modelAndView,
                                    @PageableDefault(size = 6) Pageable pageable) {
    Page<ThingsToDoMainImageServiceModel> thingsToDoPage = this.thingsToDoService.findAllPageable(pageable);
    return getModelAndView(modelAndView, thingsToDoPage);
  }
  
  @GetMapping("/province")
  public ModelAndView getThingsToDoByProvince(@ModelAttribute("bindingModel")
                                                  RegisterUserBindingModel bindingModel,
                                              ModelAndView modelAndView,
                                              @PageableDefault(size = 6) Pageable pageable,
                                              @RequestParam("province") String province){

    Page<ThingsToDoMainImageServiceModel> thingsToDoPage =
      this.thingsToDoService.findByProvincePageable(province, pageable);
    return getModelAndView(modelAndView, thingsToDoPage);
  }

  @GetMapping("/search")
  public ModelAndView getThingsToDoByName(@ModelAttribute("bindingModel")
                                                RegisterUserBindingModel bindingModel,
                                              ModelAndView modelAndView,
                                              @PageableDefault(size = 6) Pageable pageable,
                                              @RequestParam("name") String name){

    Page<ThingsToDoMainImageServiceModel> thingsToDoPage =
      this.thingsToDoService.findByNamePageable(name, pageable);
    return getModelAndView(modelAndView, thingsToDoPage);
  }

  @GetMapping("/details/{id}")
  public ModelAndView getThingToDoDetails(@ModelAttribute("bindingModel")
                                            RegisterUserBindingModel bindingModel,
                                          ModelAndView modelAndView,
                                          @PathVariable String id) {
    ThingsToDoServiceModel serviceModel = this.thingsToDoService.findById(id);
    ThingsToDoDetailsViewModel thingToDo = this.modelMapper.map(serviceModel, ThingsToDoDetailsViewModel.class);

    return view("things-todo/thing-todo-details", "thingToDo", thingToDo, modelAndView);
  }

  private ModelAndView getModelAndView(ModelAndView modelAndView, Page<ThingsToDoMainImageServiceModel> thingsToDoPage) {
    List<ThingsToDoViewModel> thingsToDo = thingsToDoPage.stream().map(thp -> this.modelMapper.map(thp,
      ThingsToDoViewModel.class)).collect(Collectors.toList());

    modelAndView.addObject("page", thingsToDoPage);
    modelAndView.addObject("provinces", this.thingsToDoService.findAllProvinces());
    return this.view("things-todo/things-todo", "thingsToDo", thingsToDo, modelAndView);
  }
}
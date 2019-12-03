package com.silenci0.philippines.web.controllers;

import com.silenci0.philippines.domain.models.binding.PlaceBindingModel;
import com.silenci0.philippines.domain.models.binding.ThingsToDoBindingModel;
import com.silenci0.philippines.domain.models.service.PlaceServiceModel;
import com.silenci0.philippines.domain.models.service.ThingsToDoAddServiceModel;
import com.silenci0.philippines.service.PlaceService;
import com.silenci0.philippines.service.ThingsToDoService;
import com.silenci0.philippines.validation.place.AddPlaceValidator;
import com.silenci0.philippines.validation.thingsToDo.AddThingToDoValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.security.Principal;

@Controller
@RequestMapping("/creators")
public class CreatorsController extends BaseController {

  private final PlaceService placeService;
  private final ThingsToDoService thingsToDoService;
  private final ModelMapper modelMapper;
  private final AddPlaceValidator placeValidator;
  private final AddThingToDoValidator addThingToDoValidator;

  @Autowired
  public CreatorsController(PlaceService placeService,
                            ThingsToDoService thingsToDoService,
                            ModelMapper modelMapper,
                            AddPlaceValidator placeValidator,
                            AddThingToDoValidator addThingToDoValidator) {
    this.placeService = placeService;
    this.thingsToDoService = thingsToDoService;
    this.modelMapper = modelMapper;
    this.placeValidator = placeValidator;
    this.addThingToDoValidator = addThingToDoValidator;
  }

  @GetMapping("")
  @PreAuthorize("hasRole('ROLE_CREATOR')")
  public ModelAndView getCreatorHome(ModelAndView modelAndView) {
    return view("creators/creator-home", modelAndView);
  }

  @GetMapping("/blog-post")
  @PreAuthorize("hasRole('ROLE_CREATOR')")
  public ModelAndView getBlogPost(ModelAndView modelAndView) {
    return view("creators/add-blog-post", modelAndView);
  }

  @GetMapping("/add-place")
  @PreAuthorize("hasRole('ROLE_CREATOR')")
  public ModelAndView getAddPlace(ModelAndView modelAndView,
                                  @ModelAttribute("bindingModel") PlaceBindingModel bindingModel) {
    return view("creators/add-place", modelAndView);
  }

  @PostMapping("/add-place")
  @PreAuthorize("hasRole('ROLE_CREATOR')")
  public ModelAndView postAddPlace(ModelAndView modelAndView,
                                   @ModelAttribute("bindingModel") PlaceBindingModel bindingModel,
                                   BindingResult bindingResult) throws IOException {
    this.placeValidator.validate(bindingModel, bindingResult);

    if (bindingResult.hasErrors()) {
      return view("creators/add-place", "bindingModel", bindingModel, modelAndView);
    }

    PlaceServiceModel placeServiceModel = this.modelMapper.map(bindingModel, PlaceServiceModel.class);

    this.placeService.savePlace(placeServiceModel);
    return redirect("/places", modelAndView);
  }

  @GetMapping("/add-picture")
  @PreAuthorize("hasRole('ROLE_CREATOR')")
  public ModelAndView getAddPicture(ModelAndView modelAndView) {
    return view("creators/add-picture", modelAndView);
  }

  @GetMapping("/add-thing-to-do")
  @PreAuthorize("hasRole('ROLE_CREATOR')")
  public ModelAndView getAddThingToDo(ModelAndView modelAndView,
                                      @ModelAttribute("bindingModel") ThingsToDoBindingModel bindingModel) {
    return view("creators/add-thing-to-do", modelAndView);
  }

  @PostMapping("/add-thing-to-do")
  @PreAuthorize("hasRole('ROLE_CREATOR')")
  public ModelAndView postAddThingToDo(ModelAndView modelAndView,
                                       Principal principal,
                                       @ModelAttribute("bindingModel")
                                           ThingsToDoBindingModel bindingModel,
                                       BindingResult bindingResult) throws IOException {
    this.addThingToDoValidator.validate(bindingModel, bindingResult);

    if (bindingResult.hasErrors()) {
      return view("creators/add-thing-to-do", "bindingModel", bindingModel, modelAndView);
    }

    ThingsToDoAddServiceModel thingsToDoAddServiceModel = this.modelMapper.map(bindingModel, ThingsToDoAddServiceModel.class);

    this.thingsToDoService.saveThing(thingsToDoAddServiceModel, principal);
    return redirect("/things-to-do", modelAndView);
  }
}

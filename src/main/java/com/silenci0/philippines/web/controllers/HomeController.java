package com.silenci0.philippines.web.controllers;

import com.silenci0.philippines.domain.models.binding.RegisterUserBindingModel;
import com.silenci0.philippines.domain.models.view.AllPlacesViewModel;
import com.silenci0.philippines.domain.models.view.PostHomeViewModel;
import com.silenci0.philippines.domain.models.view.ThingsToDoViewModel;
import com.silenci0.philippines.service.PlaceService;
import com.silenci0.philippines.service.PostService;
import com.silenci0.philippines.service.ThingsToDoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping
public class HomeController extends BaseController {

  private final PlaceService placeService;
  private final ThingsToDoService thingsToDoService;
  private final PostService postService;

  private final ModelMapper modelMapper;

  @Autowired
  public HomeController(PlaceService placeService,
                        ThingsToDoService thingsToDoService,
                        PostService postService,
                        ModelMapper modelMapper) {
    this.placeService = placeService;
    this.thingsToDoService = thingsToDoService;
    this.postService = postService;
    this.modelMapper = modelMapper;
  }

  @GetMapping("")
  public ModelAndView index(@ModelAttribute("bindingModel")
                              RegisterUserBindingModel bindingModel,
                            ModelAndView modelAndView) {

    List<AllPlacesViewModel> places = this.placeService.findNewestTakeFour()
      .stream()
      .map(p -> this.modelMapper.map(p, AllPlacesViewModel.class))
      .collect(Collectors.toList());

    List<ThingsToDoViewModel> thingsToDo = this.thingsToDoService.findNewestTakeFour()
      .stream()
      .map(p -> this.modelMapper.map(p, ThingsToDoViewModel.class))
      .collect(Collectors.toList());

    List<PostHomeViewModel> posts = this.postService.findNewestTakeFour()
      .stream()
      .map(p -> this.modelMapper.map(p, PostHomeViewModel.class))
      .collect(Collectors.toList());

    modelAndView.addObject("places", places);
    modelAndView.addObject("thingsToDo", thingsToDo);
    modelAndView.addObject("posts", posts);

    return view("index", modelAndView);
  }

  @GetMapping("/home")
  public ModelAndView home(@ModelAttribute("bindingModel") RegisterUserBindingModel bindingModel,
                           ModelAndView modelAndView) {
    return redirect("/", modelAndView);
  }

  @GetMapping("/login")
  public ModelAndView loginRedirect(@ModelAttribute("bindingModel") RegisterUserBindingModel bindingModel,
                                    ModelAndView modelAndView) {
    return redirect("/", modelAndView);
  }
}

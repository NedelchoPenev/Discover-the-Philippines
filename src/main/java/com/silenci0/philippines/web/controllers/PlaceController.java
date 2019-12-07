package com.silenci0.philippines.web.controllers;

import com.silenci0.philippines.domain.models.binding.RegisterUserBindingModel;
import com.silenci0.philippines.domain.models.service.PlaceServiceModel;
import com.silenci0.philippines.domain.models.view.AllPlacesViewModel;
import com.silenci0.philippines.domain.models.view.PlaceViewModel;
import com.silenci0.philippines.service.PlaceService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/places")
public class PlaceController extends BaseController {

  private final PlaceService placeService;
  private final ModelMapper modelMapper;

  @Autowired
  public PlaceController(PlaceService placeService, ModelMapper modelMapper) {
    this.placeService = placeService;
    this.modelMapper = modelMapper;
  }

  @GetMapping("")
  public ModelAndView getPlace(@ModelAttribute("bindingModel") RegisterUserBindingModel bindingModel,
                               ModelAndView modelAndView) {
    List<AllPlacesViewModel> places = this.placeService.findAll().stream()
      .map(place -> this.modelMapper.map(place, AllPlacesViewModel.class))
      .collect(Collectors.toList());

    return this.view("place/places", "places", places, modelAndView);
  }

  @GetMapping("/details/{id}")
  public ModelAndView getPlaceDetails(@ModelAttribute("bindingModel") RegisterUserBindingModel bindingModel,
                                      ModelAndView modelAndView, @PathVariable String id) {
    PlaceServiceModel placeServiceModel = this.placeService.findById(id);
    PlaceViewModel place = this.modelMapper.map(placeServiceModel, PlaceViewModel.class);

    modelAndView.addObject("hotel", place.getPlaceHotels());
    modelAndView.addObject("map", place.getPlaceOnMap());
    return this.view("place/place-details", "place", place, modelAndView);
  }

  @GetMapping("fetch/all")
  @ResponseBody()
  public List<AllPlacesViewModel> fetchAllPlace() {
    return this.placeService.findAll().stream()
      .map(place -> this.modelMapper.map(place, AllPlacesViewModel.class))
      .collect(Collectors.toList());
  }
}
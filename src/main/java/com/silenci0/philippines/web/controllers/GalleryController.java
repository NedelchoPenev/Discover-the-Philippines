package com.silenci0.philippines.web.controllers;

import com.silenci0.philippines.domain.models.binding.RegisterUserBindingModel;
import com.silenci0.philippines.domain.models.service.ImageServiceModel;
import com.silenci0.philippines.domain.models.view.ImageViewModel;
import com.silenci0.philippines.service.ImageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/gallery")
public class GalleryController extends BaseController {

  private final ImageService imageService;
  private final ModelMapper modelMapper;

  @Autowired
  public GalleryController(ImageService imageService, ModelMapper modelMapper) {
    this.imageService = imageService;
    this.modelMapper = modelMapper;
  }

  @GetMapping("")
  public ModelAndView getGallery(@ModelAttribute("bindingModel") RegisterUserBindingModel bindingModel,
                                 ModelAndView modelAndView) {
    return this.view("gallery/gallery", modelAndView);
  }

  @GetMapping("fetch/{category}")
  @ResponseBody()
  public Map<String, Set<ImageServiceModel>> getByCategory(@PathVariable String category) {
    return this.imageService.findAllByCategory(category);
  }

  @GetMapping("/fetch/all")
  @ResponseBody()
  public List<ImageViewModel> getAllGallery() {
    return this.imageService.findAll()
      .stream()
      .map(i -> this.modelMapper.map(i, ImageViewModel.class))
      .collect(Collectors.toList());
  }

  @GetMapping("{category}/{name}")
  @ResponseBody()
  public ModelAndView getAllFromCategoryByName(@ModelAttribute("bindingModel")
                                                   RegisterUserBindingModel bindingModel,
                                               ModelAndView modelAndView,
                                               @PathVariable String name,
                                               @PathVariable String category) {
    List<ImageViewModel> images = this.imageService.findAllFromCategoryByName(category, name)
      .stream()
      .map(i -> this.modelMapper.map(i, ImageViewModel.class))
      .collect(Collectors.toList());

    modelAndView.addObject("name", name);
    return this.view("gallery/gallery-single", "images", images, modelAndView);
  }
}

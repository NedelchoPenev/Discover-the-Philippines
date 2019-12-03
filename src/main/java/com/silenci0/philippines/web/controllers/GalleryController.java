package com.silenci0.philippines.web.controllers;

import com.silenci0.philippines.domain.models.binding.RegisterUserBindingModel;
import com.silenci0.philippines.domain.models.view.ImageViewModel;
import com.silenci0.philippines.service.ImageService;
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
@RequestMapping("/gallery")
public class GalleryController extends BaseController{
    private final ImageService imageService;
    private final ModelMapper modelMapper;

    @Autowired
    public GalleryController(ImageService imageService, ModelMapper modelMapper) {
        this.imageService = imageService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("all")
    public ModelAndView getGallery(@ModelAttribute("bindingModel") RegisterUserBindingModel bindingModel, ModelAndView modelAndView) {
        List<ImageViewModel> images = this.imageService.findAll()
          .stream()
          .map(i -> this.modelMapper.map(i, ImageViewModel.class))
          .collect(Collectors.toList());

        modelAndView.addObject("quantity", images.size());
        return this.view("gallery/gallery-all", "images", images, modelAndView);
    }

    @GetMapping("single")
    public ModelAndView getSingle(@ModelAttribute("bindingModel") RegisterUserBindingModel bindingModel, ModelAndView modelAndView) {
        return this.view("gallery/gallery-single", modelAndView);
    }
}

package com.silenci0.philippines.web.controllers;

import com.silenci0.philippines.domain.models.binding.CategoryBindingModel;
import com.silenci0.philippines.domain.models.service.CategoryServiceModel;
import com.silenci0.philippines.domain.models.view.CategoryViewModel;
import com.silenci0.philippines.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/category")
public class CategoryController extends BaseController{
  private final CategoryService categoryService;
  private final ModelMapper modelMapper;

  @Autowired
  public CategoryController(CategoryService categoryService, ModelMapper modelMapper) {
    this.categoryService = categoryService;
    this.modelMapper = modelMapper;
  }

  @PostMapping("/add-category")
  @PreAuthorize("hasRole('ROLE_CREATOR')")
  public ModelAndView addCategory(ModelAndView modelAndView,
                                  @ModelAttribute()
                                    CategoryBindingModel categoryBindingModel){
    CategoryServiceModel serviceModel =
      this.modelMapper.map(categoryBindingModel, CategoryServiceModel.class);

    this.categoryService.addCategory(serviceModel);

    return redirect("/creators/add-blog-post", modelAndView);
  }

  @GetMapping("fetch/all")
  @ResponseBody()
  public Set<CategoryViewModel> fetchCategories(){
    return this.categoryService.findAll()
      .stream()
      .map(c -> this.modelMapper.map(c, CategoryViewModel.class))
      .collect(Collectors.toSet());
  }
}

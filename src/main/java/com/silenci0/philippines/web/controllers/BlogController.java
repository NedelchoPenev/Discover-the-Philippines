package com.silenci0.philippines.web.controllers;

import com.silenci0.philippines.domain.models.binding.RegisterUserBindingModel;
import com.silenci0.philippines.domain.models.service.AllPostsServiceModel;
import com.silenci0.philippines.domain.models.view.AllPostsViewModel;
import com.silenci0.philippines.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/blog")
public class BlogController extends BaseController {

  private final PostService postService;
  private final ModelMapper modelMapper;

  @Autowired
  public BlogController(PostService postService, ModelMapper modelMapper) {
    this.postService = postService;
    this.modelMapper = modelMapper;
  }

  @GetMapping("")
  public ModelAndView getPosts(ModelAndView modelAndView,
                               @ModelAttribute("bindingModel") RegisterUserBindingModel bindingModel,
                               @PageableDefault(size = 4,
                                 sort = "datePosted",
                                 direction = Sort.Direction.DESC) Pageable pageable) {

    Page<AllPostsServiceModel> serviceModelPage = this.postService.findAllPageable(pageable);
    List<AllPostsViewModel> posts = serviceModelPage
      .stream()
      .map(p -> this.modelMapper.map(p, AllPostsViewModel.class))
      .collect(Collectors.toList());

    modelAndView.addObject("page", serviceModelPage);
    return view("blog/blog", "posts", posts, modelAndView);
  }

  @GetMapping("single")
  public ModelAndView getSingleBlog(@ModelAttribute("bindingModel") RegisterUserBindingModel bindingModel,
                                    ModelAndView modelAndView) {
    return this.view("blog/single-blog", modelAndView);
  }
}

package com.silenci0.philippines.web.controllers;

import com.silenci0.philippines.domain.models.binding.RegisterUserBindingModel;
import com.silenci0.philippines.domain.models.service.AllPostsServiceModel;
import com.silenci0.philippines.domain.models.service.PostServiceModel;
import com.silenci0.philippines.domain.models.view.AllPostsViewModel;
import com.silenci0.philippines.domain.models.view.PostViewModel;
import com.silenci0.philippines.domain.models.view.TopPostViewModel;
import com.silenci0.philippines.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
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

  @GetMapping("/search")
  public ModelAndView getPostsByKeyword(ModelAndView modelAndView,
                                      @ModelAttribute("bindingModel")
                                        RegisterUserBindingModel bindingModel,
                                      @RequestParam("keyword") String keyword,
                                      @PageableDefault(size = 4,
                                        sort = "datePosted",
                                        direction = Sort.Direction.DESC) Pageable pageable) {

    Page<AllPostsServiceModel> serviceModelPage = this.postService.findByKeyword(keyword, pageable);
    List<AllPostsViewModel> posts = serviceModelPage
      .stream()
      .map(p -> this.modelMapper.map(p, AllPostsViewModel.class))
      .collect(Collectors.toList());

    modelAndView.addObject("page", serviceModelPage);
    return view("blog/blog", "posts", posts, modelAndView);
  }

  @GetMapping("fetch/top-posts")
  @ResponseBody
  public List<TopPostViewModel> getTopPosts(){
    return this.postService.findTopPosts()
      .stream()
      .map(p -> this.modelMapper.map(p, TopPostViewModel.class))
      .collect(Collectors.toList());
  }

  @GetMapping("/category/{id}")
  public ModelAndView getPostsByCategory(@PathVariable String id,
                                         @ModelAttribute("bindingModel")
                                           RegisterUserBindingModel bindingModel,
                                         ModelAndView modelAndView,
                                         @PageableDefault(size = 4,
                                           sort = "datePosted",
                                           direction = Sort.Direction.DESC) Pageable pageable){

    Page<AllPostsServiceModel> serviceModelPage = this.postService.findPostsByCategoryId(id, pageable);
    List<AllPostsViewModel> posts = serviceModelPage
      .stream()
      .map(p -> this.modelMapper.map(p, AllPostsViewModel.class))
      .collect(Collectors.toList());

    modelAndView.addObject("page", serviceModelPage);
    return view("blog/blog", "posts", posts, modelAndView);
  }

  @GetMapping("{id}")
  public ModelAndView getSingleBlog(@ModelAttribute("bindingModel")
                                      RegisterUserBindingModel bindingModel,
                                    ModelAndView modelAndView,
                                    @PathVariable String id,
                                    Principal principal) {

    PostServiceModel serviceModel = this.postService.findById(id);
    PostViewModel post = this.modelMapper.map(serviceModel, PostViewModel.class);

    boolean isLikeable = false;
    if (principal != null){
      isLikeable = !post.getLikes().contains(principal.getName());
    }

    modelAndView.addObject("isLikeable", isLikeable);
    return this.view("blog/blog-details","post", post, modelAndView);
  }

  @GetMapping("/{postId}/like")
  @PreAuthorize("isAuthenticated()")
  public ModelAndView likePost(@ModelAttribute("bindingModel")
                                   RegisterUserBindingModel bindingModel,
                               ModelAndView modelAndView,
                               @PathVariable String postId,
                               Principal principal){

    this.postService.addLikeToPost(postId, principal.getName());

    return redirect("/blog/" + postId, modelAndView);
  }

  @PostMapping("/comment/{postId}")
  @PreAuthorize("isAuthenticated()")
  public ModelAndView postComment(@ModelAttribute("bindingModel")
                                      RegisterUserBindingModel bindingModel,
                                  ModelAndView modelAndView,
                                  @RequestParam("comment") String comment,
                                  @PathVariable String postId,
                                  Principal principal){

    this.postService.addCommentToPost(postId, comment, principal.getName());

    return redirect("/blog/" + postId, modelAndView);
  }
}

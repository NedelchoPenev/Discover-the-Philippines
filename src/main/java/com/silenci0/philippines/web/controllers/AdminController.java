package com.silenci0.philippines.web.controllers;

import com.silenci0.philippines.domain.models.binding.*;
import com.silenci0.philippines.domain.models.service.*;
import com.silenci0.philippines.domain.models.view.*;
import com.silenci0.philippines.service.*;
import com.silenci0.philippines.validation.image.EditImageValidator;
import com.silenci0.philippines.validation.place.EditPlaceValidator;
import com.silenci0.philippines.validation.post.PostEditValidator;
import com.silenci0.philippines.validation.thingsToDo.EditThingToDoValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController {

  private final UserServiceImpl userService;
  private final PlaceService placeService;
  private final ThingsToDoService thingsToDoService;
  private final ImageService imageService;
  private final CategoryService categoryService;
  private final PostService postService;

  private final ModelMapper modelMapper;

  private final EditPlaceValidator placeValidator;
  private final EditThingToDoValidator editThingToDoValidator;
  private final EditImageValidator editImageValidator;
  private final PostEditValidator postEditValidator;

  @Autowired
  public AdminController(UserServiceImpl userService,
                         PlaceService placeService,
                         ThingsToDoService thingsToDoService,
                         ImageService imageService,
                         CategoryService categoryService,
                         PostService postService,
                         ModelMapper modelMapper,
                         EditPlaceValidator placeValidator,
                         EditThingToDoValidator editThingToDoValidator,
                         EditImageValidator editImageValidator,
                         PostEditValidator postEditValidator) {
    this.userService = userService;
    this.placeService = placeService;
    this.thingsToDoService = thingsToDoService;
    this.imageService = imageService;
    this.categoryService = categoryService;
    this.postService = postService;
    this.modelMapper = modelMapper;
    this.placeValidator = placeValidator;
    this.editThingToDoValidator = editThingToDoValidator;
    this.editImageValidator = editImageValidator;
    this.postEditValidator = postEditValidator;
  }

  @GetMapping("")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ModelAndView getAdminHome(ModelAndView modelAndView) {
    return this.view("admin/admin-home", modelAndView);
  }

  @GetMapping("/edit-users")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ModelAndView allUsers(ModelAndView modelAndView,
                               Principal principal,
                               @PageableDefault(size = 5) Pageable pageable) {
    Page<UsersPageServiceModel> pageUsers = this.userService.findAllUsers(principal, pageable);
    return mapUserAndAuthorities(modelAndView, pageUsers);
  }

  @GetMapping("/edit-user")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ModelAndView editUser(ModelAndView modelAndView,
                               @RequestParam("username") String username,
                               Principal principal,
                               @PageableDefault(size = 20) Pageable pageable) {
    Page<UsersPageServiceModel> pageUsers =
      this.userService.findByUsernamePage(username, principal.getName(), pageable);
    return mapUserAndAuthorities(modelAndView, pageUsers);
  }

  @GetMapping("/edit-users/delete/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ModelAndView deleteUsername(ModelAndView modelAndView, @PathVariable String id) {
    this.userService.deleteById(id);
    return redirect("/admin/edit-users", modelAndView);
  }

  @PostMapping("/edit-users/set-role/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ModelAndView setRole(@PathVariable String id,
                              @RequestParam("role") String role,
                              ModelAndView modelAndView) {
    this.userService.setUserRole(id, role);
    return redirect("/admin/edit-users", modelAndView);
  }

  @GetMapping("/edit-places")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ModelAndView getEditPlaces(ModelAndView modelAndView, @PageableDefault(size = 5) Pageable pageable) {
    Page<AllPlacesServiceModel> pagePlace = this.placeService.findAllPageable(pageable);
    List<AllPlacesViewModel> places = pagePlace.stream()
      .map(place -> this.modelMapper.map(place, AllPlacesViewModel.class)).collect(Collectors.toList());

    modelAndView.addObject("page", pagePlace);
    return view("admin/edit-places", "places", places, modelAndView);
  }

  @GetMapping("/edit-place")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ModelAndView getEditPlace(ModelAndView modelAndView,
                                   @RequestParam("name") String name,
                                   @PageableDefault(size = 20) Pageable pageable) {
    Page<AllPlacesServiceModel> pagePlace = this.placeService.findByNamePlace(name, pageable);
    List<AllPlacesViewModel> places = pagePlace.stream()
      .map(place -> this.modelMapper.map(place, AllPlacesViewModel.class)).collect(Collectors.toList());

    modelAndView.addObject("page", pagePlace);
    return view("admin/edit-places", "places", places, modelAndView);
  }

  @GetMapping("/edit-place/delete/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ModelAndView deletePlace(ModelAndView modelAndView, @PathVariable String id) {
    this.placeService.deleteById(id);
    return redirect("/admin/edit-places", modelAndView);
  }

  @GetMapping("/edit-place/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ModelAndView editPlace(@PathVariable String id,
                                ModelAndView modelAndView) {
    PlaceServiceModel placeServiceModel = this.placeService.findById(id);
    PlaceBindingModel bindingModel = this.modelMapper.map(placeServiceModel, PlaceBindingModel.class);

    return view("admin/edit-place", "bindingModel", bindingModel, modelAndView);
  }

  @PostMapping("/edit-place/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ModelAndView postEditPlace(@PathVariable String id,
                                    ModelAndView modelAndView,
                                    @ModelAttribute("bindingModel") PlaceBindingModel bindingModel,
                                    BindingResult bindingResult,
                                    Principal principal) throws IOException {
    this.placeValidator.validate(bindingModel, bindingResult);

    if (bindingResult.hasErrors()) {
      return view("admin/edit-place", "bindingModel", bindingModel, modelAndView);
    }

    PlaceServiceModel placeServiceModel = this.modelMapper.map(bindingModel, PlaceServiceModel.class);

    this.placeService.editPlace(id, placeServiceModel, principal);

    return redirect("/places/details/" + id, modelAndView);
  }

  @GetMapping("/edit-things-to-do")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ModelAndView getEditThingToDo(ModelAndView modelAndView,
                                       @PageableDefault(size = 5) Pageable pageable) {

    Page<ThingsToDoMainImageServiceModel> pageThingsToDo = this.thingsToDoService.findAllPageable(pageable);
    List<AllThingsToDoViewModel> thingsToDo = pageThingsToDo.stream()
      .map(place -> this.modelMapper.map(place, AllThingsToDoViewModel.class))
      .collect(Collectors.toList());

    modelAndView.addObject("page", pageThingsToDo);
    return view("admin/edit-things-to-do", "thingsToDo", thingsToDo, modelAndView);
  }

  @GetMapping("/edit-thing-to-do/delete/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ModelAndView deleteThingToDo(ModelAndView modelAndView, @PathVariable String id) {
    this.thingsToDoService.deleteById(id);
    return redirect("/admin/edit-things-to-do", modelAndView);
  }

  @GetMapping("/edit-thing-to-do")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ModelAndView getEditThingToDoByName(ModelAndView modelAndView,
                                             @RequestParam("name") String name,
                                             @PageableDefault(size = 20) Pageable pageable) {
    Page<ThingsToDoMainImageServiceModel> pageThings = this.thingsToDoService.findByNamePageable(name, pageable);
    List<ThingsToDoViewModel> thingsToDo = pageThings.stream().map(thp -> this.modelMapper.map(thp,
      ThingsToDoViewModel.class)).collect(Collectors.toList());

    modelAndView.addObject("page", pageThings);
    return view("admin/edit-things-to-do", "thingsToDo", thingsToDo, modelAndView);
  }

  @GetMapping("/edit-thing-to-do/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ModelAndView editThingToDo(@PathVariable String id,
                                    ModelAndView modelAndView) {
    ThingsToDoDeleteImageServiceModel thingsToDoServiceModel = this.thingsToDoService.findByIdEdit(id);
    ThingsToDoBindingModel bindingModel =
      this.modelMapper.map(thingsToDoServiceModel, ThingsToDoBindingModel.class);

    modelAndView.addObject("thingToDoId", thingsToDoServiceModel.getId());
    return view("admin/edit-thing-to-do", "bindingModel", bindingModel, modelAndView);
  }

  @PostMapping("/edit-thing-to-do/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ModelAndView postEditThingToDo(ModelAndView modelAndView, @PathVariable String id,
                                        @ModelAttribute("bindingModel")
                                          ThingsToDoEditBindingModel bindingModel,
                                        Principal principal,
                                        BindingResult bindingResult) throws IOException {

    this.editThingToDoValidator.validate(bindingModel, bindingResult);

    if (bindingResult.hasErrors()) {
      return view("admin/edit-thing-to-do", "bindingModel", bindingModel, modelAndView);
    }

    ThingsToDoEditServiceModel serviceModel =
      this.modelMapper.map(bindingModel, ThingsToDoEditServiceModel.class);
    this.thingsToDoService.editThingToDo(id, serviceModel, principal);

    return redirect("/things-to-do/details/" + id, modelAndView);
  }


  @PostMapping("/edit-thing-to-do/delete-picture")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @ResponseBody()
  public String deleteThingToDoImage(@RequestParam("thingToDoId") String thingToDoId,
                                     @RequestParam("imageId") String imageId) {
    return this.thingsToDoService.deleteImageById(thingToDoId, imageId);
  }

  @GetMapping("/edit-pictures")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ModelAndView getEditPictures(ModelAndView modelAndView,
                                      @PageableDefault(size = 5,
                                        sort = "uploadDate",
                                        direction = Sort.Direction.DESC)
                                        Pageable pageable) {
    Page<AllPicturesServiceModel> pagePictures = this.imageService.findAllPageable(pageable);
    List<AllPicturesViewModel> pictures = pagePictures.stream()
      .map(place -> this.modelMapper.map(place, AllPicturesViewModel.class))
      .collect(Collectors.toList());

    modelAndView.addObject("page", pagePictures);
    return view("admin/edit-pictures", "pictures", pictures, modelAndView);
  }

  @GetMapping("/edit-picture/delete/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ModelAndView deletePicture(ModelAndView modelAndView, @PathVariable String id) throws IOException {
    this.imageService.deleteById(id);
    return redirect("/admin/edit-pictures", modelAndView);
  }

  @GetMapping("/edit-picture/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ModelAndView getEditPicture(ModelAndView modelAndView, @PathVariable String id) {
    ImageEditBindingModel bindingModel = this.modelMapper.map(this.imageService.findById(id),
      ImageEditBindingModel.class);

    return view("/admin/edit-picture", "bindingModel", bindingModel, modelAndView);
  }

  @PostMapping("/edit-picture/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ModelAndView postEditPicture(ModelAndView modelAndView,
                                      @PathVariable String id,
                                      @ModelAttribute("bindingModel")
                                        ImageEditBindingModel bindingModel,
                                      BindingResult bindingResult) {

    this.editImageValidator.validate(bindingModel, bindingResult);

    if (bindingResult.hasErrors()) {
      return view("admin/edit-picture", "bindingModel", bindingModel, modelAndView);
    }

    ImageEditServiceModel serviceModel =
      this.modelMapper.map(bindingModel, ImageEditServiceModel.class);

    this.imageService.editImage(id, serviceModel);

    return redirect("/admin/edit-pictures", modelAndView);
  }

  @GetMapping("/edit-categories")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ModelAndView getEditCategories(ModelAndView modelAndView, Pageable pageable) {
    Page<CategoryServiceModel> categoriesPage = this.categoryService.findAllPageable(pageable);
    Set<CategoryViewModel> categories = categoriesPage.stream()
      .map(c -> this.modelMapper.map(c, CategoryViewModel.class))
      .collect(Collectors.toSet());

    modelAndView.addObject("page", categoriesPage);
    return view("admin/edit-categories", "categories", categories, modelAndView);
  }

  @GetMapping("/edit-category/delete/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ModelAndView deleteCategory(ModelAndView modelAndView, @PathVariable String id) {
    this.categoryService.deleteById(id);
    return redirect("/admin/edit-categories", modelAndView);
  }

  @GetMapping("/edit-category/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ModelAndView getEditCategory(ModelAndView modelAndView, @PathVariable String id) {
    CategoryServiceModel serviceModel = this.categoryService.findById(id);
    CategoryViewModel category = this.modelMapper.map(serviceModel, CategoryViewModel.class);
    return view("/admin/edit-category", "category", category, modelAndView);
  }

  @PostMapping("/edit-category/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ModelAndView postEditCategory(ModelAndView modelAndView,
                                       @ModelAttribute("category")
                                         CategoryBindingModel categoryBindingModel,
                                       @PathVariable String id) {
    CategoryServiceModel serviceModel =
      this.modelMapper.map(categoryBindingModel, CategoryServiceModel.class);

    this.categoryService.editCategory(serviceModel, id);

    return redirect("/admin/edit-categories", modelAndView);
  }

  @GetMapping("/edit-blog-posts")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ModelAndView getEditBlogPosts(ModelAndView modelAndView,
                                       @PageableDefault(size = 5,
                                         sort = "datePosted",
                                         direction = Sort.Direction.DESC) Pageable pageable) {

    Page<AllPostsServiceModel> postsPage = this.postService.findAllPageable(pageable);
    List<PostsEditViewModel> posts = postsPage
      .stream()
      .map(p -> this.modelMapper.map(p, PostsEditViewModel.class))
      .collect(Collectors.toList());

    modelAndView.addObject("page", postsPage);
    return view("admin/edit-blog-posts", "posts", posts, modelAndView);
  }

  @GetMapping("/edit-blog-post/title")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ModelAndView getEditBlogPostByTitle(ModelAndView modelAndView,
                                             @RequestParam("title") String title,
                                             @PageableDefault(size = 20,
                                               sort = "datePosted",
                                               direction = Sort.Direction.DESC) Pageable pageable) {

    Page<AllPostsServiceModel> postsPage = this.postService.findByKeyword(title, pageable);
    List<PostsEditViewModel> posts = postsPage
      .stream()
      .map(p -> this.modelMapper.map(p, PostsEditViewModel.class))
      .collect(Collectors.toList());

    modelAndView.addObject("page", postsPage);
    return view("admin/edit-blog-posts", "posts", posts, modelAndView);
  }

  @GetMapping("/edit-blog-post/delete/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ModelAndView getDeleteBlogPost(ModelAndView modelAndView, @PathVariable String id) {
    this.postService.deleteById(id);

    return redirect("/admin/edit-blog-posts", modelAndView);
  }

  @GetMapping("/edit-blog-post/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ModelAndView getEditBlogPost(ModelAndView modelAndView,
                                      @PathVariable String id) {
    PostEditServiceModel serviceModel = this.postService.findByIdEdit(id);
    PostEditBindingModel post = this.modelMapper.map(serviceModel, PostEditBindingModel.class);

    modelAndView.addObject("categories", post.getCategories());
    return view("/admin/edit-blog-post", "bindingModel", post, modelAndView);
  }

  @PostMapping("/edit-blog-post/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ModelAndView postEditBlogPost(ModelAndView modelAndView,
                                       @PathVariable String id,
                                       @ModelAttribute("bindingModel")
                                           PostEditBindingModel bindingModel,
                                       Principal principal,
                                       BindingResult bindingResult) throws IOException {

    this.postEditValidator.validate(bindingModel, bindingResult);

    if (bindingResult.hasErrors()) {
      return view("admin/edit-blog-post", "bindingModel", bindingModel, modelAndView);
    }

    PostEditServiceModel serviceModel = this.modelMapper.map(bindingModel, PostEditServiceModel.class);

    this.postService.editPost(id, serviceModel, principal.getName());

    return redirect("/blog/" + id, modelAndView);
  }

  private ModelAndView mapUserAndAuthorities(ModelAndView modelAndView, Page<UsersPageServiceModel> pageUsers) {
    List<UsersAllViewModel> users = pageUsers
      .getContent()
      .stream()
      .map(u -> {
        UsersAllViewModel user = this.modelMapper.map(u, UsersAllViewModel.class);
        Set<String> authorities =
          u.getAuthorities().stream().map(RoleServiceModel::getAuthority).collect(Collectors.toSet());
        user.setAuthorities(authorities);

        return user;
      })
      .filter(u -> !u.getAuthorities().contains("ROLE_ROOT"))
      .collect(Collectors.toList());

    modelAndView.addObject("page", pageUsers);
    return view("admin/edit-users", "users", users, modelAndView);
  }

  @GetMapping("/edit-blog-comments/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ModelAndView getEditBlogComments(ModelAndView modelAndView,
                                      @PathVariable String id) {
    PostEditCommentsServiceModel serviceModel = this.postService.findByEditCommentsId(id);
    PostEditCommentsViewModel post = this.modelMapper.map(serviceModel, PostEditCommentsViewModel.class);

    return view("admin/edit-blog-comments", "post", post, modelAndView);
  }

  @GetMapping("/edit-blog-comment/{postId}/delete/{commentId}")
  public ModelAndView deleteCommentFromPost(ModelAndView modelAndView,
                                            @PathVariable String postId,
                                            @PathVariable String commentId){

    this.postService.deleteComment(postId, commentId);

    return redirect("/admin/edit-blog-comments/" + postId, modelAndView);
  }
}
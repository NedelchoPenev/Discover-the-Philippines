package com.silenci0.philippines.web.controllers;

import com.silenci0.philippines.domain.models.binding.PlaceBindingModel;
import com.silenci0.philippines.domain.models.binding.ThingsToDoBindingModel;
import com.silenci0.philippines.domain.models.binding.ThingsToDoEditBindingModel;
import com.silenci0.philippines.domain.models.service.*;
import com.silenci0.philippines.domain.models.view.AllPlacesViewModel;
import com.silenci0.philippines.domain.models.view.AllThingsToDoViewModel;
import com.silenci0.philippines.domain.models.view.ThingsToDoViewModel;
import com.silenci0.philippines.domain.models.view.UsersAllViewModel;
import com.silenci0.philippines.service.PlaceService;
import com.silenci0.philippines.service.ThingsToDoService;
import com.silenci0.philippines.service.UserServiceImpl;
import com.silenci0.philippines.validation.place.EditPlaceValidator;
import com.silenci0.philippines.validation.thingsToDo.EditThingToDoValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
  private final ModelMapper modelMapper;
  private final EditPlaceValidator placeValidator;
  private final EditThingToDoValidator editThingToDoValidator;

  @Autowired
  public AdminController(UserServiceImpl userService,
                         PlaceService placeService,
                         ThingsToDoService thingsToDoService,
                         ModelMapper modelMapper,
                         EditPlaceValidator placeValidator,
                         EditThingToDoValidator editThingToDoValidator) {
    this.userService = userService;
    this.placeService = placeService;
    this.thingsToDoService = thingsToDoService;
    this.modelMapper = modelMapper;
    this.placeValidator = placeValidator;
    this.editThingToDoValidator = editThingToDoValidator;
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
                                    BindingResult bindingResult) throws IOException {
    this.placeValidator.validate(bindingModel, bindingResult);

    if (bindingResult.hasErrors()) {
      return view("admin/edit-place", "bindingModel", bindingModel, modelAndView);
    }

    PlaceServiceModel placeServiceModel = this.modelMapper.map(bindingModel, PlaceServiceModel.class);

    this.placeService.editPlace(id, placeServiceModel);

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

    return redirect("/things-to-do", modelAndView);
  }

  @PostMapping("/edit-thing-to-do/delete-picture")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @ResponseBody()
  public String deleteThingToDoImage(@RequestParam("thingToDoId") String thingToDoId,
                                     @RequestParam("imageId") String imageId){
    return this.thingsToDoService.deleteImageById(thingToDoId, imageId);
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
      .collect(Collectors.toList());

    modelAndView.addObject("page", pageUsers);
    return view("admin/edit-users", "users", users, modelAndView);
  }
}
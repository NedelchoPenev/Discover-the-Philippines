package com.silenci0.philippines.web.controllers;

import com.silenci0.philippines.domain.models.binding.EditProfileBindingModel;
import com.silenci0.philippines.domain.models.binding.RegisterUserBindingModel;
import com.silenci0.philippines.domain.models.service.UserEditServiceModel;
import com.silenci0.philippines.domain.models.service.UserServiceModel;
import com.silenci0.philippines.domain.models.view.EditProfileViewModel;
import com.silenci0.philippines.domain.models.view.UserProfileViewModel;
import com.silenci0.philippines.service.CloudinaryService;
import com.silenci0.philippines.service.UserService;
import com.silenci0.philippines.validation.user.UserEditValidator;
import com.silenci0.philippines.validation.user.UserRegisterValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;

@Controller
@RequestMapping("/users")
public class UserController extends BaseController {

  private final AuthenticationManager authenticationManager;
  private final UserService userService;
  private final ModelMapper modelMapper;
  private final UserRegisterValidator userRegisterValidator;
  private final UserEditValidator userEditValidator;
  private final CloudinaryService cloudinaryService;

  @Autowired
  public UserController(AuthenticationManager authenticationManager, UserService userService, ModelMapper modelMapper
      , UserRegisterValidator userRegisterValidator
      , UserEditValidator userEditValidator, CloudinaryService cloudinaryService) {
    this.authenticationManager = authenticationManager;
    this.userService = userService;
    this.modelMapper = modelMapper;
    this.userRegisterValidator = userRegisterValidator;
    this.userEditValidator = userEditValidator;
    this.cloudinaryService = cloudinaryService;
  }

  @PostMapping("/register")
  @PreAuthorize("isAnonymous()")
  public ModelAndView registerConfirm(HttpServletRequest request,
                                      @Valid @ModelAttribute("bindingModel")
                                          RegisterUserBindingModel bindingModel,
                                      BindingResult bindingResult, ModelAndView modelAndView) {
    this.userRegisterValidator.validate(bindingModel, bindingResult);

    if (bindingResult.hasErrors()) {
      bindingModel.setPassword(null);
      bindingModel.setConfirmPassword(null);

      return view("index", "bindingModel", bindingModel, modelAndView);
    }

    UserServiceModel userServiceModel = this.modelMapper.map(bindingModel, UserServiceModel.class);
    this.userService.registerUser(userServiceModel);

    UsernamePasswordAuthenticationToken authToken =
        new UsernamePasswordAuthenticationToken(userServiceModel.getUsername(), userServiceModel.getPassword());
    authToken.setDetails(new WebAuthenticationDetails(request));

    Authentication authentication = this.authenticationManager.authenticate(authToken);

    SecurityContextHolder.getContext().setAuthentication(authentication);

    return redirect("/", modelAndView);
  }

  @GetMapping("/edit-profile")
  @PreAuthorize("isAuthenticated()")
  public ModelAndView editProfile(Principal principal, ModelAndView modelAndView) {
    UserEditServiceModel userEditServiceModel = this.userService.findUserEditByUserName(principal.getName());
    EditProfileViewModel bindingModel = this.modelMapper.map(userEditServiceModel, EditProfileViewModel.class);

    return view("user/edit-profile", "bindingModel", bindingModel, modelAndView);
  }

  @PatchMapping("/edit-profile")
  @PreAuthorize("isAuthenticated()")
  public ModelAndView editProfileConfirm(ModelAndView modelAndView,
                                         @ModelAttribute("bindingModel")
                                             EditProfileBindingModel bindingModel,
                                         BindingResult bindingResult) throws IOException {
    this.userEditValidator.validate(bindingModel, bindingResult);

    if (bindingResult.hasErrors()) {
      bindingModel.setCurrentPassword(null);
      bindingModel.setNewPassword(null);
      bindingModel.setConfirmPassword(null);

      return view("user/edit-profile", "bindingModel", bindingModel, modelAndView);
    }

    UserEditServiceModel userEditServiceModel =
        this.modelMapper.map(bindingModel, UserEditServiceModel.class);
    if (!bindingModel.getProfilePicture().isEmpty()) {
      userEditServiceModel.setProfilePictureUrl(this.cloudinaryService.uploadImage(bindingModel.getProfilePicture()));
    }
    this.userService.editUserProfile(userEditServiceModel, bindingModel.getCurrentPassword());

    return redirect("/users/profile/" + userEditServiceModel.getUsername(), modelAndView);
  }

  @GetMapping("/profile/{username}")
  @PreAuthorize("isAuthenticated()")
  public ModelAndView getProfile(@PathVariable String username, ModelAndView modelAndView){
    UserEditServiceModel userEditServiceModel = this.userService.findUserEditByUserName(username);
    UserProfileViewModel bindingModel = this.modelMapper.map(userEditServiceModel, UserProfileViewModel.class);

    return view("user/profile", "bindingModel", bindingModel, modelAndView);
  }
}


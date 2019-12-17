package com.silenci0.philippines.service;

import com.silenci0.philippines.domain.entities.User;
import com.silenci0.philippines.domain.models.service.UserAboutServiceModel;
import com.silenci0.philippines.domain.models.service.UserEditServiceModel;
import com.silenci0.philippines.domain.models.service.UserServiceModel;
import com.silenci0.philippines.domain.models.service.UsersPageServiceModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.security.Principal;
import java.util.List;

public interface UserService extends UserDetailsService {

  UserServiceModel registerUser(UserServiceModel userServiceModel);

  UserServiceModel findUserByUserName(String username);

  User findUserByUsername(String username);

  UserEditServiceModel findUserEditByUserName(String username);

  UserEditServiceModel editUserProfile(UserEditServiceModel userEditServiceModel, String oldPassword);

  Page<UsersPageServiceModel> findAllUsers(Principal principal, Pageable pageable);

  Page<UsersPageServiceModel> findByUsernamePage(String username, String usernameNot, Pageable pageable);

  void deleteById(String id);

  void setUserRole(String id, String role);

  List<UserAboutServiceModel> findCreators();
}

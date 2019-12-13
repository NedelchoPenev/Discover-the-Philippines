package com.silenci0.philippines.service;

import com.silenci0.philippines.domain.entities.Role;
import com.silenci0.philippines.domain.entities.User;
import com.silenci0.philippines.domain.models.service.UserAboutServiceModel;
import com.silenci0.philippines.domain.models.service.UserEditServiceModel;
import com.silenci0.philippines.domain.models.service.UserServiceModel;
import com.silenci0.philippines.domain.models.service.UsersPageServiceModel;
import com.silenci0.philippines.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService, ApplicationListener<AuthenticationSuccessEvent> {

  private static final String USERNAME_NOT_FOUND = "Username not found!";
  private static final String INCORRECT_PASSWORD = "Incorrect password!";
  private static final String INCORRECT_ID = "Incorrect id!";
  private static final String DEFAULT_PROFILE_PICTURE = "https://res.cloudinary.com/duddzgxsd/image/upload/v1534965846/important/j418yffqf62ps0umrr7l.png";

  private final UserRepository userRepository;
  private final RoleService roleService;
  private final ModelMapper modelMapper;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  public UserServiceImpl(UserRepository userRepository,
                         RoleService roleService,
                         ModelMapper modelMapper,
                         BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.userRepository = userRepository;
    this.roleService = roleService;
    this.modelMapper = modelMapper;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  @Override
  public UserServiceModel registerUser(UserServiceModel userServiceModel) {
    this.roleService.seedRolesInDb();
    if (this.userRepository.count() == 0) {
      userServiceModel.setAuthorities(this.roleService.findAllRoles());
    } else {
      userServiceModel.setAuthorities(new LinkedHashSet<>());

      userServiceModel.getAuthorities().add(this.roleService.findByAuthority("ROLE_USER"));
    }

    User user = this.modelMapper.map(userServiceModel, User.class);
    user.setPassword(this.bCryptPasswordEncoder.encode(userServiceModel.getPassword()));
    LocalDate registrationDate = LocalDate.now();
    user.setRegistrationDate(registrationDate);
    LocalDateTime lastDateLogin = LocalDateTime.now();
    user.setLastDateLogin(lastDateLogin);
    user.setProfilePictureUrl(DEFAULT_PROFILE_PICTURE);

    return this.modelMapper.map(this.userRepository.saveAndFlush(user), UserServiceModel.class);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return this.userRepository
      .findByUsername(username)
      .orElseThrow(() -> new UsernameNotFoundException(USERNAME_NOT_FOUND));
  }

  @Override
  public UserServiceModel findUserByUserName(String username) {
    return this.userRepository.findByUsername(username)
      .map(u -> this.modelMapper.map(u, UserServiceModel.class))
      .orElseThrow(() -> new UsernameNotFoundException(USERNAME_NOT_FOUND));
  }

  @Override
  public UserEditServiceModel findUserEditByUserName(String username) {
    return this.userRepository.findByUsername(username)
      .map(u -> this.modelMapper.map(u, UserEditServiceModel.class))
      .orElseThrow(() -> new UsernameNotFoundException(USERNAME_NOT_FOUND));
  }

  @Override
  public UserEditServiceModel editUserProfile(UserEditServiceModel userEditServiceModel, String oldPassword) {
    User user = this.userRepository.findByUsername(userEditServiceModel.getUsername())
      .orElseThrow(() -> new UsernameNotFoundException(USERNAME_NOT_FOUND));

    if (!this.bCryptPasswordEncoder.matches(oldPassword, user.getPassword())) {
      throw new IllegalArgumentException(INCORRECT_PASSWORD);
    }

    user.setFullName(userEditServiceModel.getFullName());
    user.setEmail(userEditServiceModel.getEmail());
    user.setAboutMe(userEditServiceModel.getAboutMe());
    user.setPassword(!userEditServiceModel.getPassword().equals("") ?
      this.bCryptPasswordEncoder.encode(userEditServiceModel.getPassword()) :
      user.getPassword());
    user.setProfilePictureUrl(userEditServiceModel.getProfilePictureUrl());

    return this.modelMapper.map(this.userRepository.saveAndFlush(user), UserEditServiceModel.class);
  }

  @Override
  public Page<UsersPageServiceModel> findAllUsers(Principal principal, Pageable pageable) {
    return this.userRepository.findDistinctByUsernameNot(principal.getName(), pageable)
      .map(user -> this.modelMapper.map(user, UsersPageServiceModel.class));
  }

  @Override
  public Page<UsersPageServiceModel> findByUsernamePage(String username, String usernameNot, Pageable pageable) {
    return this.userRepository
      .findDistinctByUsernameStartingWithAndUsernameNot(username, usernameNot, pageable)
      .map(user -> this.modelMapper.map(user, UsersPageServiceModel.class));
  }

  @Override
  public List<UserAboutServiceModel> findCreators() {
    Role roleCreator = this.roleService.findByAuthorityReturnRole("ROLE_CREATOR");
    return this.userRepository.findAllByAuthorities(roleCreator)
      .stream()
      .sorted(Comparator.comparing(User::getRegistrationDate))
      .map(u -> this.modelMapper.map(u, UserAboutServiceModel.class))
      .collect(Collectors.toList());
  }

  @Override
  public void deleteById(String id) {
    this.userRepository.deleteById(id);
  }

  @Override
  public void setUserRole(String id, String role) {
    User user = this.userRepository.findById(id)
      .orElseThrow(() -> new IllegalArgumentException(INCORRECT_ID));

    Set<String> userRoles = user.getAuthorities().stream().map(Role::getAuthority).collect(Collectors.toSet());
    if (userRoles.contains("ROLE_ROOT")) {
      throw new IllegalArgumentException("Cannot edit ROOT");
    }

    UserEditServiceModel userServiceModel = this.modelMapper.map(user, UserEditServiceModel.class);
    userServiceModel.getAuthorities().clear();

    switch (role) {
      case "user":
        userServiceModel.getAuthorities().add(this.roleService.findByAuthority("ROLE_USER"));
        break;
      case "creator":
        userServiceModel.getAuthorities().add(this.roleService.findByAuthority("ROLE_USER"));
        userServiceModel.getAuthorities().add(this.roleService.findByAuthority("ROLE_CREATOR"));
        break;
      case "admin":
        userServiceModel.getAuthorities().add(this.roleService.findByAuthority("ROLE_USER"));
        userServiceModel.getAuthorities().add(this.roleService.findByAuthority("ROLE_CREATOR"));
        userServiceModel.getAuthorities().add(this.roleService.findByAuthority("ROLE_ADMIN"));
        break;
    }

    this.userRepository.saveAndFlush(this.modelMapper.map(userServiceModel, User.class));
  }

  @Override
  public void onApplicationEvent(AuthenticationSuccessEvent event) {
    String username = ((UserDetails) event.getAuthentication().getPrincipal()).getUsername();
    User user = this.userRepository.findByUsername(username)
      .orElseThrow(() -> new UsernameNotFoundException(USERNAME_NOT_FOUND));
    user.setLastDateLogin(LocalDateTime.now());
    this.userRepository.save(user);
  }
}
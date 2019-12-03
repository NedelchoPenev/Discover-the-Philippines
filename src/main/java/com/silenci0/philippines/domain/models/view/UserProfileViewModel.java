package com.silenci0.philippines.domain.models.view;

import com.silenci0.philippines.domain.models.service.RoleServiceModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public class UserProfileViewModel {
  private String username;

  private String fullName;

  private String aboutMe;

  private String email;

  private String country;

  private LocalDate registrationDate;

  private LocalDateTime lastDateLogin;

  private String profilePictureUrl;

  private Set<RoleServiceModel> authorities;

  public UserProfileViewModel() {
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getAboutMe() {
    return aboutMe;
  }

  public void setAboutMe(String aboutMe) {
    this.aboutMe = aboutMe;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public LocalDate getRegistrationDate() {
    return registrationDate;
  }

  public void setRegistrationDate(LocalDate registrationDate) {
    this.registrationDate = registrationDate;
  }

  public LocalDateTime getLastDateLogin() {
    return lastDateLogin;
  }

  public void setLastDateLogin(LocalDateTime lastDateLogin) {
    this.lastDateLogin = lastDateLogin;
  }

  public String getProfilePictureUrl() {
    return profilePictureUrl;
  }

  public void setProfilePictureUrl(String profilePictureUrl) {
    this.profilePictureUrl = profilePictureUrl;
  }

  public Set<RoleServiceModel> getAuthorities() {
    return authorities;
  }

  public void setAuthorities(Set<RoleServiceModel> authorities) {
    this.authorities = authorities;
  }
}

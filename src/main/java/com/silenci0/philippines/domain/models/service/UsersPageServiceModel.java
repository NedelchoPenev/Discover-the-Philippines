package com.silenci0.philippines.domain.models.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public class UsersPageServiceModel {
  private String id;

  private String username;

  private LocalDate registrationDate;

  private LocalDateTime lastDateLogin;

  private String profilePictureUrl;

  private Set<RoleServiceModel> authorities;

  public UsersPageServiceModel() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
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

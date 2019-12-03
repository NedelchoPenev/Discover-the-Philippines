package com.silenci0.philippines.domain.models.binding;

import com.silenci0.philippines.domain.models.service.RoleServiceModel;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public class EditProfileBindingModel {

  private String username;

  private String fullName;

  private String aboutMe;

  private String currentPassword;
  private String newPassword;
  private String confirmPassword;

  private String email;

  private String country;

  private LocalDate registrationDate;

  private LocalDateTime lastDateLogin;

  private MultipartFile profilePicture;
  private String profilePictureUrl;

  private Set<RoleServiceModel> authorities;

  public EditProfileBindingModel() {
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getCurrentPassword() {
    return currentPassword;
  }

  public void setCurrentPassword(String currentPassword) {
    this.currentPassword = currentPassword;
  }

  public String getNewPassword() {
    return newPassword;
  }

  public void setNewPassword(String newPassword) {
    this.newPassword = newPassword;
  }

  public String getConfirmPassword() {
    return confirmPassword;
  }

  public void setConfirmPassword(String confirmPassword) {
    this.confirmPassword = confirmPassword;
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

  public MultipartFile getProfilePicture() {
    return profilePicture;
  }

  public void setProfilePicture(MultipartFile profilePicture) {
    this.profilePicture = profilePicture;
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
}

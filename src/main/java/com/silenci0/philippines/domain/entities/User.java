package com.silenci0.philippines.domain.entities;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@DynamicInsert()
@DynamicUpdate()
public class User extends BaseEntity implements UserDetails {

  private String username;
  private String fullName;
  private String password;
  private String email;
  private String country;
  private String aboutMe;
  private LocalDate registrationDate;
  private LocalDateTime lastDateLogin;
  private String profilePictureUrl;
  private Set<Role> authorities;
  private Set<Post> posts = new HashSet<>();
  private Set<Comment> comments = new HashSet<>();

  public User() {
  }

  @Override
  @Column(name = "username", nullable = false, unique = true)
  @Size(min = 3, max = 20)
  public String getUsername() {
    return this.username;
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

  @Override
  @Column(name = "password", nullable = false)
  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Column(name = "email", nullable = false, unique = true)
  public String getEmail() {
    return this.email;
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

  @Column(length = 65535, columnDefinition = "Text")
  public String getAboutMe() {
    return aboutMe;
  }

  public void setAboutMe(String aboutMe) {
    this.aboutMe = aboutMe;
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

  @Override
  @ManyToMany(targetEntity = Role.class, fetch = FetchType.EAGER)
  @JoinTable(
    name = "users_roles",
    joinColumns = @JoinColumn(
      name = "user_id",
      referencedColumnName = "id"
    ),
    inverseJoinColumns = @JoinColumn(
      name = "role_id",
      referencedColumnName = "id"
    )
  )
  public Set<Role> getAuthorities() {
    return this.authorities;
  }

  public void setAuthorities(Set<Role> authorities) {
    this.authorities = authorities;
  }

  @OneToMany(targetEntity = Post.class)
  public Set<Post> getPosts() {
    return posts;
  }

  public void setPosts(Set<Post> posts) {
    this.posts = posts;
  }

  @OneToMany(targetEntity = Comment.class, cascade = CascadeType.ALL, orphanRemoval = true)
  public Set<Comment> getComments() {
    return comments;
  }

  public void setComments(Set<Comment> comments) {
    this.comments = comments;
  }

  @Override
  @Transient
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  @Transient
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  @Transient
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  @Transient
  public boolean isEnabled() {
    return true;
  }

  public void addPost(Post post) {
    this.posts.add(post);
  }

  public void removePost(Post post) {
      this.posts.remove(post);
  }

  public void addComment(Comment comment) {
    this.comments.add(comment);
  }

  public void removeComment(Comment comment) {
    this.comments.remove(comment);
  }
}

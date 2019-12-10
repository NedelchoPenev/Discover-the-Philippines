package com.silenci0.philippines.domain.models.service;

import com.silenci0.philippines.domain.models.view.CategoryViewModel;
import com.silenci0.philippines.domain.models.view.CommentViewModel;
import com.silenci0.philippines.domain.models.view.UserViewModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class PostServiceModel extends BaseServiceModel{
  private String title;
  private String headerImageUrl;
  private Set<CategoryViewModel> categories;
  private String article;
  private LocalDateTime datePosted;
  private UserViewModel author;

  private Set<String> likes;
  private List<CommentViewModel> comments;

  public PostServiceModel() {
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getHeaderImageUrl() {
    return headerImageUrl;
  }

  public void setHeaderImageUrl(String headerImageUrl) {
    this.headerImageUrl = headerImageUrl;
  }

  public Set<CategoryViewModel> getCategories() {
    return categories;
  }

  public void setCategories(Set<CategoryViewModel> categories) {
    this.categories = categories;
  }

  public String getArticle() {
    return article;
  }

  public void setArticle(String article) {
    this.article = article;
  }

  public LocalDateTime getDatePosted() {
    return datePosted;
  }

  public void setDatePosted(LocalDateTime datePosted) {
    this.datePosted = datePosted;
  }

  public UserViewModel getAuthor() {
    return author;
  }

  public void setAuthor(UserViewModel author) {
    this.author = author;
  }

  public Set<String> getLikes() {
    return likes;
  }

  public void setLikes(Set<String> likes) {
    this.likes = likes;
  }

  public List<CommentViewModel> getComments() {
    return comments;
  }

  public void setComments(List<CommentViewModel> comments) {
    this.comments = comments;
  }
}

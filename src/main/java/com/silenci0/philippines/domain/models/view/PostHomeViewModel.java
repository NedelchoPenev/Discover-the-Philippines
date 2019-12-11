package com.silenci0.philippines.domain.models.view;

import java.time.LocalDateTime;
import java.util.Set;

public class PostHomeViewModel {

  private String id;
  private String title;
  private String headerImageUrl;
  private Set<CategoryViewModel> categories;
  private String article;
  private LocalDateTime datePosted;

  public PostHomeViewModel() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
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
}

package com.silenci0.philippines.domain.models.view;

import java.time.LocalDateTime;
import java.util.Set;

public class AllPostsViewModel {

  private String id;
  private String title;
  private String headerImageUrl;
  private String article;
  private Set<String> categories;
  private Integer commentsSize;
  private LocalDateTime datePosted;

  public AllPostsViewModel() {
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

  public String getArticle() {
    return article;
  }

  public void setArticle(String article) {
    this.article = article;
  }

  public Set<String> getCategories() {
    return categories;
  }

  public void setCategories(Set<String> categories) {
    this.categories = categories;
  }

  public Integer getCommentsSize() {
    return commentsSize;
  }

  public void setCommentsSize(Integer commentsSize) {
    this.commentsSize = commentsSize;
  }

  public LocalDateTime getDatePosted() {
    return datePosted;
  }

  public void setDatePosted(LocalDateTime datePosted) {
    this.datePosted = datePosted;
  }
}

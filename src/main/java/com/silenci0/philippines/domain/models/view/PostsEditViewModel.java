package com.silenci0.philippines.domain.models.view;

import java.time.LocalDateTime;

public class PostsEditViewModel {

  private String id;
  private String title;
  private LocalDateTime datePosted;

  public PostsEditViewModel() {
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

  public LocalDateTime getDatePosted() {
    return datePosted;
  }

  public void setDatePosted(LocalDateTime datePosted) {
    this.datePosted = datePosted;
  }
}

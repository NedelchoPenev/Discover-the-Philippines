package com.silenci0.philippines.domain.models.view;

import java.time.LocalDateTime;

public class TopPostViewModel {
  private String id;
  private String title;
  private String headerImageUrl;
  private Integer likesSize;
  private LocalDateTime datePosted;

  public TopPostViewModel() {
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

  public Integer getLikesSize() {
    return likesSize;
  }

  public void setLikesSize(Integer likesSize) {
    this.likesSize = likesSize;
  }

  public LocalDateTime getDatePosted() {
    return datePosted;
  }

  public void setDatePosted(LocalDateTime datePosted) {
    this.datePosted = datePosted;
  }
}

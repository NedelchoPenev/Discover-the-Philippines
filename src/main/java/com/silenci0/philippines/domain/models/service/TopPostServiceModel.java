package com.silenci0.philippines.domain.models.service;

import java.time.LocalDateTime;

public class TopPostServiceModel extends BaseServiceModel{
  private String title;
  private String headerImageUrl;
  private Integer likesSize;
  private LocalDateTime datePosted;

  public TopPostServiceModel() {
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

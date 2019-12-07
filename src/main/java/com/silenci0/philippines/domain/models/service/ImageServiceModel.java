package com.silenci0.philippines.domain.models.service;

import java.time.LocalDateTime;

public class ImageServiceModel extends BaseServiceModel{
  private String url;
  private String place;
  private String province;
  private LocalDateTime uploadDate;
  private String uploaderUsername;

  public ImageServiceModel() {
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getPlace() {
    return place;
  }

  public void setPlace(String place) {
    this.place = place;
  }

  public String getProvince() {
    return province;
  }

  public void setProvince(String province) {
    this.province = province;
  }

  public LocalDateTime getUploadDate() {
    return uploadDate;
  }

  public void setUploadDate(LocalDateTime uploadDate) {
    this.uploadDate = uploadDate;
  }

  public String getUploaderUsername() {
    return uploaderUsername;
  }

  public void setUploaderUsername(String uploaderUsername) {
    this.uploaderUsername = uploaderUsername;
  }
}

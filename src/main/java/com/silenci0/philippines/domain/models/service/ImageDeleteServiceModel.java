package com.silenci0.philippines.domain.models.service;

public class ImageDeleteServiceModel{
  private String id;
  private String public_id;
  private String place;
  private String province;
  private String url;

  public ImageDeleteServiceModel(String id, String url) {
    this.id = id;
    this.url = url;
  }

  public ImageDeleteServiceModel() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getPublic_id() {
    return public_id;
  }

  public void setPublic_id(String public_id) {
    this.public_id = public_id;
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

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}

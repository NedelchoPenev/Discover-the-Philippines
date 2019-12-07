package com.silenci0.philippines.domain.models.service;

public class ImageEditServiceModel extends BaseServiceModel{
  private String url;
  private String place;
  private String placeNew;
  private String province;

  public ImageEditServiceModel() {
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

  public String getPlaceNew() {
    return placeNew;
  }

  public void setPlaceNew(String placeNew) {
    this.placeNew = placeNew;
  }

  public String getProvince() {
    return province;
  }

  public void setProvince(String province) {
    this.province = province;
  }
}

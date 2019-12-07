package com.silenci0.philippines.domain.models.binding;

public class ImageEditBindingModel {
  private String id;
  private String url;
  private String place;
  private String placeNew;
  private String province;

  public ImageEditBindingModel() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
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

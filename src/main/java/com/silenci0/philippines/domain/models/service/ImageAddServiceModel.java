package com.silenci0.philippines.domain.models.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ImageAddServiceModel {
  private List<MultipartFile> images;
  private String place;
  private String placeNew;
  private String province;

  public ImageAddServiceModel() {
  }

  public List<MultipartFile> getImages() {
    return images;
  }

  public void setImages(List<MultipartFile> images) {
    this.images = images;
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

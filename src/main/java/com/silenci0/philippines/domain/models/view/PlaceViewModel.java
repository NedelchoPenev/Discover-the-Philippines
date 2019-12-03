package com.silenci0.philippines.domain.models.view;

public class PlaceViewModel {
  private String id;
  private String name;
  private String headerImageUrl;
  private String province;
  private String info;
  private String placeHotels;
  private String placeOnMap;
  private String article;

  public PlaceViewModel() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getHeaderImageUrl() {
    return headerImageUrl;
  }

  public void setHeaderImageUrl(String headerImageUrl) {
    this.headerImageUrl = headerImageUrl;
  }

  public String getProvince() {
    return province;
  }

  public void setProvince(String province) {
    this.province = province;
  }

  public String getInfo() {
    return info;
  }

  public void setInfo(String info) {
    this.info = info;
  }

  public String getPlaceHotels() {
    return placeHotels;
  }

  public void setPlaceHotels(String placeHotels) {
    this.placeHotels = placeHotels;
  }

  public String getPlaceOnMap() {
    return placeOnMap;
  }

  public void setPlaceOnMap(String placeOnMap) {
    this.placeOnMap = placeOnMap;
  }

  public String getArticle() {
    return article;
  }

  public void setArticle(String article) {
    this.article = article;
  }
}

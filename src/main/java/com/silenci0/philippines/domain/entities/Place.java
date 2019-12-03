package com.silenci0.philippines.domain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "place")
public class Place extends BaseEntity {

  private String name;
  private String headerImageUrl;
  private String province;
  private String info;
  private String placeHotels;
  private String placeOnMap;
  private String article;

  public Place() {
  }

  @Column(nullable = false, unique = true)
  @Size(min = 3, max = 20)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Column(nullable = false, unique = true)
  public String getHeaderImageUrl() {
    return headerImageUrl;
  }

  public void setHeaderImageUrl(String headerImageUrl) {
    this.headerImageUrl = headerImageUrl;
  }

  @Column(nullable = false)
  public String getProvince() {
    return province;
  }

  public void setProvince(String province) {
    this.province = province;
  }

  @Column(nullable = false)
  @Size(min = 50, max = 255)
  public String getInfo() {
    return info;
  }

  public void setInfo(String info) {
    this.info = info;
  }

  @Column(columnDefinition="Text")
  public String getPlaceHotels() {
    return placeHotels;
  }

  public void setPlaceHotels(String placeHotels) {
    this.placeHotels = placeHotels;
  }

  @Column(columnDefinition="Text")
  public String getPlaceOnMap() {
    return placeOnMap;
  }

  public void setPlaceOnMap(String placeOnMap) {
    this.placeOnMap = placeOnMap;
  }

  @Column(length = 65535, columnDefinition="Text", nullable = false)
  public String getArticle() {
    return article;
  }

  public void setArticle(String article) {
    this.article = article;
  }
}

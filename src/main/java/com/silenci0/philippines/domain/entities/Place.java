package com.silenci0.philippines.domain.entities;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "place")
public class Place extends BaseEntity {

  private String name;
  private Image headerImage;
  private String province;
  private String info;
  private String placeHotels;
  private String placeOnMap;
  private String article;
  private LocalDateTime dateAdded;

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

  @OneToOne(targetEntity = Image.class, optional = false, cascade = CascadeType.ALL)
  public Image getHeaderImage() {
    return headerImage;
  }

  public void setHeaderImage(Image headerImage) {
    this.headerImage = headerImage;
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
  @Size(min = 50)
  public String getArticle() {
    return article;
  }

  public void setArticle(String article) {
    this.article = article;
  }

  public LocalDateTime getDateAdded() {
    return dateAdded;
  }

  public void setDateAdded(LocalDateTime dateAdded) {
    this.dateAdded = dateAdded;
  }
}

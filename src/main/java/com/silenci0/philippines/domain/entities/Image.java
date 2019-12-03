package com.silenci0.philippines.domain.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "image")
public class Image extends BaseEntity {

  private String url;
  private String public_id;
  private String place;
  private String province;
  private LocalDateTime uploadDate;
  private User uploader;

  public Image() {
  }

  @Column(unique = true, nullable = false)
  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  @Column(unique = true, nullable = false)
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

  @Column(unique = true, nullable = false)
  public LocalDateTime getUploadDate() {
    return uploadDate;
  }

  public void setUploadDate(LocalDateTime uploadDate) {
    this.uploadDate = uploadDate;
  }

  @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
  @JoinTable(name = "uploader", joinColumns =
  @JoinColumn(
    name = "image_id",
    referencedColumnName = "id"
  ),
    inverseJoinColumns = @JoinColumn(
      name = "user_id",
      referencedColumnName = "id"
    ))
  public User getUploader() {
    return uploader;
  }

  public void setUploader(User uploader) {
    this.uploader = uploader;
  }
}

package com.silenci0.philippines.domain.entities;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "things_to_do")
public class ThingsToDo extends BaseEntity {

  private String name;
  private List<Image> imagesUrls;
  private String mainImageUrl;
  private String province;
  private String overview;
  private String externalLink;

  public ThingsToDo() {
  }

  @Column(unique = true, nullable = false)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
  @JoinTable(name = "images", joinColumns = @JoinColumn(
    name = "things_to_do_id",
    referencedColumnName = "id"
  ), inverseJoinColumns = @JoinColumn(
    name = "image_id",
    referencedColumnName = "id"
  ))
  public List<Image> getImagesUrls() {
    return imagesUrls;
  }

  public void setImagesUrls(List<Image> imagesUrls) {
    this.imagesUrls = imagesUrls;
  }

  public String getMainImageUrl() {
    return mainImageUrl;
  }

  public void setMainImageUrl(String mainImageUrl) {
    this.mainImageUrl = mainImageUrl;
  }

  @Column(nullable = false)
  public String getProvince() {
    return province;
  }

  public void setProvince(String province) {
    this.province = province;
  }

  @Column(nullable = false, columnDefinition = "TEXT")
  @Size(min = 30)
  public String getOverview() {
    return overview;
  }

  public void setOverview(String overview) {
    this.overview = overview;
  }

  public String getExternalLink() {
    return externalLink;
  }

  public void setExternalLink(String externalLink) {
    this.externalLink = externalLink;
  }
}

package com.silenci0.philippines.domain.models.view;

import java.util.List;

public class ThingsToDoDetailsViewModel {
  private String name;
  private List<String> imagesUrls;
  private String mainImageUrl;
  private String province;
  private String overview;
  private String externalLink;

  public ThingsToDoDetailsViewModel() {
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<String> getImagesUrls() {
    return imagesUrls;
  }

  public void setImagesUrls(List<String> imagesUrls) {
    this.imagesUrls = imagesUrls;
  }

  public String getMainImageUrl() {
    return mainImageUrl;
  }

  public void setMainImageUrl(String mainImageUrl) {
    this.mainImageUrl = mainImageUrl;
  }

  public String getProvince() {
    return province;
  }

  public void setProvince(String province) {
    this.province = province;
  }

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

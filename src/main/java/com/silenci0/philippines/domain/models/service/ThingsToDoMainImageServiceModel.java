package com.silenci0.philippines.domain.models.service;

public class ThingsToDoMainImageServiceModel extends BaseServiceModel {
  private String name;
  private String mainImageUrl;
  private String province;
  private String overview;
  private String externalLink;

  public ThingsToDoMainImageServiceModel() {
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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

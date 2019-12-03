package com.silenci0.philippines.domain.models.binding;

public class ImageBindingModel {
  private String id;
  private String public_id;
  private String url;

  public ImageBindingModel() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getPublic_id() {
    return public_id;
  }

  public void setPublic_id(String public_id) {
    this.public_id = public_id;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}

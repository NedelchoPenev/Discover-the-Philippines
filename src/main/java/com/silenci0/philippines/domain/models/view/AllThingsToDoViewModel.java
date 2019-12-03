package com.silenci0.philippines.domain.models.view;

public class AllThingsToDoViewModel {
  private String id;
  private String name;
  private String mainImageUrl;

  public AllThingsToDoViewModel() {
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

  public String getMainImageUrl() {
    return mainImageUrl;
  }

  public void setMainImageUrl(String mainImageUrl) {
    this.mainImageUrl = mainImageUrl;
  }
}

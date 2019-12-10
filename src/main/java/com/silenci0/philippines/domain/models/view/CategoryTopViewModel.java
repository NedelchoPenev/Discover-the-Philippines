package com.silenci0.philippines.domain.models.view;

public class CategoryTopViewModel {

  private String id;
  private String name;
  private Integer postsSize;

  public CategoryTopViewModel() {
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

  public Integer getPostsSize() {
    return postsSize;
  }

  public void setPostsSize(Integer postsSize) {
    this.postsSize = postsSize;
  }
}

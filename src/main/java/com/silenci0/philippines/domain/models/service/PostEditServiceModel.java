package com.silenci0.philippines.domain.models.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public class PostEditServiceModel extends BaseServiceModel{
  private String title;
  private String headerImageUrl;
  private MultipartFile headerImage;
  private Set<String> categories;
  private String article;

  public PostEditServiceModel() {
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getHeaderImageUrl() {
    return headerImageUrl;
  }

  public void setHeaderImageUrl(String headerImageUrl) {
    this.headerImageUrl = headerImageUrl;
  }

  public MultipartFile getHeaderImage() {
    return headerImage;
  }

  public void setHeaderImage(MultipartFile headerImage) {
    this.headerImage = headerImage;
  }

  public Set<String> getCategories() {
    return categories;
  }

  public void setCategories(Set<String> categories) {
    this.categories = categories;
  }

  public String getArticle() {
    return article;
  }

  public void setArticle(String article) {
    this.article = article;
  }
}

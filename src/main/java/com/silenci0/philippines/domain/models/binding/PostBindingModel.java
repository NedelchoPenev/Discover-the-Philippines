package com.silenci0.philippines.domain.models.binding;

import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public class PostBindingModel {
  private String title;
  private MultipartFile headerImage;
  private Set<String> categories;
  private String article;

  public PostBindingModel() {
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
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

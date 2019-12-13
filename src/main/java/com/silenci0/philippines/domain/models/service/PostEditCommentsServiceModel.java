package com.silenci0.philippines.domain.models.service;

import com.silenci0.philippines.domain.models.view.CommentViewModel;

import java.util.List;

public class PostEditCommentsServiceModel extends BaseServiceModel{
  private String title;
  private String headerImageUrl;
  private String article;

  private List<CommentViewModel> comments;

  public PostEditCommentsServiceModel() {
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

  public String getArticle() {
    return article;
  }

  public void setArticle(String article) {
    this.article = article;
  }

  public List<CommentViewModel> getComments() {
    return comments;
  }

  public void setComments(List<CommentViewModel> comments) {
    this.comments = comments;
  }
}

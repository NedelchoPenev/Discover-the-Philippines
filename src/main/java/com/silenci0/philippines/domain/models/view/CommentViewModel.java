package com.silenci0.philippines.domain.models.view;

import java.time.LocalDateTime;

public class CommentViewModel {
  private String id;
  private String content;
  private UserViewModel commenter;
  private LocalDateTime dateCommented;

  public CommentViewModel() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public UserViewModel getCommenter() {
    return commenter;
  }

  public void setCommenter(UserViewModel commenter) {
    this.commenter = commenter;
  }

  public LocalDateTime getDateCommented() {
    return dateCommented;
  }

  public void setDateCommented(LocalDateTime dateCommented) {
    this.dateCommented = dateCommented;
  }
}

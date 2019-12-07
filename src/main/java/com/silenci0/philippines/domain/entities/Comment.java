package com.silenci0.philippines.domain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
public class Comment extends BaseEntity{
  private String content;
  private User commenter;
  private LocalDateTime dateCommented;

  public Comment() {
  }

  @Column(nullable = false, columnDefinition = "TEXT")
  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  @ManyToOne(targetEntity = User.class)
  public User getCommenter() {
    return commenter;
  }

  public void setCommenter(User commenter) {
    this.commenter = commenter;
  }

  @Column(nullable = false)
  public LocalDateTime getDateCommented() {
    return dateCommented;
  }

  public void setDateCommented(LocalDateTime dateCommented) {
    this.dateCommented = dateCommented;
  }
}

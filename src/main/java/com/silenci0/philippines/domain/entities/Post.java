package com.silenci0.philippines.domain.entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "post")
public class Post extends BaseEntity{
  private String title;
  private Image headerImage;
  private Set<PostCategory> categories;
  private String article;
  private LocalDateTime datePosted;
  private User author;

  private Set<User> likes;
  private Set<Comment> comments;

  public Post() {
    this.categories = new HashSet<>();
  }

  @Column(nullable = false)
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @OneToOne(targetEntity = Image.class, optional = false, cascade = CascadeType.PERSIST)
  public Image getHeaderImage() {
    return headerImage;
  }

  public void setHeaderImage(Image headerImage) {
    this.headerImage = headerImage;
  }

  @ManyToMany(targetEntity = PostCategory.class, cascade = CascadeType.ALL)
  @JoinTable(
    name = "posts_categories",
    joinColumns = @JoinColumn(
      name = "post_id",
      referencedColumnName = "id"
    ),
    inverseJoinColumns = @JoinColumn(
      name = "category_id",
      referencedColumnName = "id"
    )
  )
  public Set<PostCategory> getCategories() {
    return categories;
  }

  public void setCategories(Set<PostCategory> categories) {
    this.categories = categories;
  }

  @Column(nullable = false)
  public LocalDateTime getDatePosted() {
    return datePosted;
  }

  public void setDatePosted(LocalDateTime datePosted) {
    this.datePosted = datePosted;
  }

  @ManyToMany
  public Set<User> getLikes() {
    return likes;
  }

  public void setLikes(Set<User> likes) {
    this.likes = likes;
  }

  @ManyToOne(targetEntity = User.class)
  public User getAuthor() {
    return author;
  }

  public void setAuthor(User author) {
    this.author = author;
  }

  @Column(nullable = false, columnDefinition = "TEXT")
  public String getArticle() {
    return article;
  }

  public void setArticle(String article) {
    this.article = article;
  }

  @OneToMany(targetEntity = Comment.class, cascade = CascadeType.ALL, orphanRemoval = true)
  public Set<Comment> getComments() {
    return comments;
  }

  public void setComments(Set<Comment> comments) {
    this.comments = comments;
  }

  public void addCategory(PostCategory category) {
    this.categories.add(category);
    category.addPost(this);
  }

  public void removeCategory(PostCategory category) {
    this.categories.remove(category);
    category.removePost(this);
  }
}

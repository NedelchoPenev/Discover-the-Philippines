package com.silenci0.philippines.domain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "category")
public class PostCategory extends BaseEntity{
  private String name;
  private Set<Post> posts;

  public PostCategory() {
    this.posts = new HashSet<>();
  }

  @Column(unique = true, nullable = false)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @ManyToMany(targetEntity = Post.class)
  public Set<Post> getPosts() {
    return posts;
  }

  public void setPosts(Set<Post> posts) {
    this.posts = posts;
  }

  public void addPost(Post post){
    this.posts.add(post);
  }

  public void removePost(Post post){
    this.posts.remove(post);
  }
}

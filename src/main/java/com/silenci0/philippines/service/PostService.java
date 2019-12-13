package com.silenci0.philippines.service;

import com.silenci0.philippines.domain.models.binding.PostEditBindingModel;
import com.silenci0.philippines.domain.models.service.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

public interface PostService {

  void savePost(PostAddServiceModel serviceModel, Principal principal) throws IOException;

  Page<AllPostsServiceModel> findAllPageable(Pageable pageable);

  Page<AllPostsServiceModel> findByKeyword(String keyword, Pageable pageable);

  List<TopPostServiceModel> findTopPosts();

  Page<AllPostsServiceModel> findPostsByCategoryId(String id, Pageable pageable);

  PostServiceModel findById(String id);

  void addLikeToPost(String postId, String likerUsername);

  void addCommentToPost(String postId, String comment, String username);

  List<PostServiceModel> findNewestTakeFour();

  Page<AllPostsServiceModel> findByTitlePageable(String title, Pageable pageable);

  void deleteById(String id);

  PostEditServiceModel findByIdEdit(String id);

  void editPost(String id, PostEditBindingModel bindingModel, String name) throws IOException;

  PostEditCommentsServiceModel findByEditCommentsId(String id);

  void deleteComment(String postId, String commentId);
}

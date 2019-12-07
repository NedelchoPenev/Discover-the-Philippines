package com.silenci0.philippines.service;

import com.silenci0.philippines.domain.entities.Image;
import com.silenci0.philippines.domain.entities.Post;
import com.silenci0.philippines.domain.entities.PostCategory;
import com.silenci0.philippines.domain.entities.User;
import com.silenci0.philippines.domain.models.service.AllPostsServiceModel;
import com.silenci0.philippines.domain.models.service.PostAddServiceModel;
import com.silenci0.philippines.repository.CategoryRepository;
import com.silenci0.philippines.repository.PostRepository;
import com.silenci0.philippines.repository.UserRepository;
import org.jsoup.Jsoup;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final CategoryRepository categoryRepository;
  private final CloudinaryService cloudinaryService;

  private final ModelMapper modelMapper;

  @Autowired
  public PostServiceImpl(PostRepository postRepository,
                         UserRepository userRepository,
                         CategoryRepository categoryRepository,
                         ModelMapper modelMapper,
                         CloudinaryService cloudinaryService) {
    this.postRepository = postRepository;
    this.userRepository = userRepository;
    this.categoryRepository = categoryRepository;
    this.modelMapper = modelMapper;
    this.cloudinaryService = cloudinaryService;
  }

  @Override
  public void savePost(PostAddServiceModel serviceModel, Principal principal) throws IOException {
    User user = this.userRepository.findByUsername(principal.getName()).orElseThrow();
    Post post = this.modelMapper.map(serviceModel, Post.class);
    if (!serviceModel.getHeaderImage().isEmpty()){
      Map response = this.cloudinaryService.uploadImageGetFullResponse(serviceModel.getHeaderImage());
      Image image = new Image();
      image.setUrl(response.get("secure_url").toString());
      image.setPublic_id(response.get("public_id").toString());
      image.setUploadDate(LocalDateTime.now());
      image.setUploader(user);
      post.setHeaderImage(image);
    }
    post.setDatePosted(LocalDateTime.now());
    post.setAuthor(user);

    for (String categoryId : serviceModel.getCategories()) {
      PostCategory category = this.categoryRepository.getOne(categoryId);
      post.addCategory(category);
    }

    this.postRepository.saveAndFlush(post);
  }

  @Override
  public Page<AllPostsServiceModel> findAllPageable(Pageable pageable) {
    return this.postRepository.findAll(pageable)
      .map(p -> {
        AllPostsServiceModel serviceModel = this.modelMapper.map(p, AllPostsServiceModel.class);
        serviceModel.setCategories(p.getCategories()
          .stream()
          .map(PostCategory::getName)
          .collect(Collectors.toSet()));

        if (serviceModel.getCommentsSize() == null) {
          serviceModel.setCommentsSize(0);
        } else {
          serviceModel.setCommentsSize(p.getComments().size());
        }
        serviceModel.setArticle(Jsoup.parse(p.getArticle()).text());
        return serviceModel;
      });
  }
}

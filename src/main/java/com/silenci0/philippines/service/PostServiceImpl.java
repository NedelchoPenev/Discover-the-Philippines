package com.silenci0.philippines.service;

import com.silenci0.philippines.domain.entities.*;
import com.silenci0.philippines.domain.models.service.*;
import com.silenci0.philippines.domain.models.view.CommentViewModel;
import com.silenci0.philippines.repository.CommentRepository;
import com.silenci0.philippines.repository.PostRepository;
import org.jsoup.Jsoup;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
  private static final String INCORRECT_ID = "There is no post with that id!";
  private static final String INCORRECT_CATEGORY_ID = "There is no category with that id!";

  private final PostRepository postRepository;
  private final UserService userService;
  private final CategoryService categoryService;
  private final CommentRepository commentRepository;
  private final CloudinaryService cloudinaryService;

  private final ModelMapper modelMapper;

  @Autowired
  public PostServiceImpl(PostRepository postRepository,
                         UserService userService,
                         CategoryService categoryService, CommentRepository commentRepository,
                         ModelMapper modelMapper,
                         CloudinaryService cloudinaryService) {
    this.postRepository = postRepository;
    this.userService = userService;
    this.categoryService = categoryService;
    this.commentRepository = commentRepository;
    this.modelMapper = modelMapper;
    this.cloudinaryService = cloudinaryService;
  }

  @Override
  public void savePost(PostAddServiceModel serviceModel, Principal principal) throws IOException {
    User user = this.userService.findUserByUsername(principal.getName());
    Post post = this.modelMapper.map(serviceModel, Post.class);

    setImage(post, user, serviceModel.getHeaderImage());

    post.setDatePosted(LocalDateTime.now());
    post.setAuthor(user);

    for (String categoryId : serviceModel.getCategories()) {
      PostCategory category = this.categoryService.findByIdWithoutMap(categoryId);
      post.addCategory(category);
    }

    user.addPost(post);

    this.postRepository.saveAndFlush(post);
  }

  @Override
  public Page<AllPostsServiceModel> findAllPageable(Pageable pageable) {
    return this.postRepository.findAll(pageable)
      .map(this::mapToServiceModel);
  }

  @Override
  public Page<AllPostsServiceModel> findByKeyword(String keyword, Pageable pageable) {
    return this.postRepository.findByTitleContains(keyword, pageable)
      .map(this::mapToServiceModel);
  }

  @Override
  public List<TopPostServiceModel> findTopPosts() {
    return this.postRepository.findAll()
      .stream()
      .map(p -> {
        TopPostServiceModel serviceModel = this.modelMapper.map(p, TopPostServiceModel.class);
        if (p.getLikes().isEmpty()) {
          serviceModel.setLikesSize(0);
        } else {
          serviceModel.setLikesSize(p.getLikes().size());
        }

        return serviceModel;
      })
      .sorted((p1, p2) -> p2.getLikesSize().compareTo(p1.getLikesSize()))
      .limit(5)
      .collect(Collectors.toList());
  }

  @Override
  public Page<AllPostsServiceModel> findPostsByCategoryId(String id, Pageable pageable) {
    List<AllPostsServiceModel> posts = this.categoryService.findByIdWithoutMap(id)
      .getPosts()
      .stream()
      .map(this::mapToServiceModel)
      .collect(Collectors.toList());

    return new PageImpl<>(posts, pageable, posts.size());
  }

  @Override
  public PostServiceModel findById(String id) {
    Post post = this.postRepository.findById(id)
      .orElseThrow(() -> new IllegalArgumentException(INCORRECT_ID));
    PostServiceModel serviceModel = this.modelMapper.map(post, PostServiceModel.class);

    Set<String> likes =
      post.getLikes().stream().map(User::getUsername).collect(Collectors.toSet());
    serviceModel.setLikes(likes);

    List<CommentViewModel> commentsSorted = sortComments(post);

    serviceModel.setComments(commentsSorted);

    return serviceModel;
  }

  @Override
  public PostEditServiceModel findByIdEdit(String id) {
    Post post = this.postRepository.findById(id)
      .orElseThrow(() -> new IllegalArgumentException(INCORRECT_ID));

    PostEditServiceModel serviceModel = this.modelMapper.map(post, PostEditServiceModel.class);
    serviceModel.setCategories(
      post.getCategories().stream().map(BaseEntity::getId).collect(Collectors.toSet()));

    return serviceModel;
  }

  @Override
  public void editPost(String id, PostEditServiceModel model, String username) throws IOException {
    Post post = this.postRepository.findById(id)
      .orElseThrow(() -> new IllegalArgumentException(INCORRECT_ID));
    User user = this.userService.findUserByUsername(username);

    this.modelMapper.map(model, post);

    setImage(post, user, model.getHeaderImage());

    for (PostCategory category : post.getCategories()) {
      category.removePost(post);
    }

    Set<PostCategory> categories = new HashSet<>();
    for (String categoryId : model.getCategories()) {
      PostCategory category = this.categoryService.findByIdWithoutMap(categoryId);
      category.addPost(post);
      categories.add(category);
    }

    post.setCategories(categories);

    this.postRepository.saveAndFlush(post);
  }

  @Override
  public void addLikeToPost(String postId, String likerUsername) {
    Post post = this.postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException(INCORRECT_ID));
    User user = this.userService.findUserByUsername(likerUsername);
    post.addLike(user);

    this.postRepository.saveAndFlush(post);
  }

  @Override
  public void addCommentToPost(String postId, String comment, String likerUsername) {
    Post post = this.postRepository.findById(postId)
      .orElseThrow(() -> new IllegalArgumentException(INCORRECT_ID));
    User user = this.userService.findUserByUsername(likerUsername);
    Comment commentToPost = new Comment();
    commentToPost.setContent(comment.trim());
    commentToPost.setCommenter(user);
    commentToPost.setDateCommented(LocalDateTime.now());
    post.addComment(commentToPost, user);

    this.commentRepository.save(commentToPost);

    this.postRepository.saveAndFlush(post);
  }

  @Override
  public List<PostServiceModel> findNewestTakeThree() {
    return this.postRepository
      .findAll(Sort.by(Sort.Direction.DESC, "datePosted"))
      .stream()
      .map(p -> {
        PostServiceModel serviceModel = this.modelMapper.map(p, PostServiceModel.class);
        serviceModel.setArticle(Jsoup.parse(p.getArticle()).text());

        return serviceModel;
      })
      .limit(3)
      .collect(Collectors.toList());
  }

  @Override
  public void deleteById(String id) {
    Post post = this.postRepository.findById(id)
      .orElseThrow(() -> new IllegalArgumentException(INCORRECT_ID));

    post.getAuthor().removePost(post);
    for (PostCategory category : post.getCategories()) {
      category.removePost(post);
    }

    for (Comment comment : post.getComments()) {
      comment.getCommenter().removeComment(comment);
    }

    post.setComments(null);

    this.postRepository.delete(post);
  }

  @Override
  public PostEditCommentsServiceModel findByEditCommentsId(String id) {
    Post post = this.postRepository.findById(id)
      .orElseThrow(() -> new IllegalArgumentException(INCORRECT_ID));

    PostEditCommentsServiceModel serviceModel =
      this.modelMapper.map(post, PostEditCommentsServiceModel.class);
    serviceModel.setArticle(Jsoup.parse(post.getArticle()).text());

    List<CommentViewModel> commentsSorted = sortComments(post);

    serviceModel.setComments(commentsSorted);

    return serviceModel;
  }

  @Override
  public void deleteComment(String postId, String commentId) {
    Post post = this.postRepository.findById(postId)
      .orElseThrow(() -> new IllegalArgumentException(INCORRECT_ID));
    Comment comment = this.commentRepository.findById(commentId)
      .orElseThrow(() -> new IllegalArgumentException(INCORRECT_CATEGORY_ID));

    post.removeComment(comment, comment.getCommenter());

    this.postRepository.saveAndFlush(post);
  }

  private void setImage(Post post, User user, MultipartFile headerImage) throws IOException {
    if (!headerImage.isEmpty()) {
      Map response = this.cloudinaryService.uploadImageGetFullResponse(headerImage);
      Image image = new Image();
      image.setUrl(response.get("secure_url").toString());
      image.setPublic_id(response.get("public_id").toString());
      image.setUploadDate(LocalDateTime.now());
      image.setUploader(user);
      post.setHeaderImage(image);
    }
  }

  private List<CommentViewModel> sortComments(Post post) {
    return post.getComments()
      .stream()
      .sorted((c1, c2) -> c2.getDateCommented().compareTo(c1.getDateCommented()))
      .map(c -> this.modelMapper.map(c, CommentViewModel.class))
      .collect(Collectors.toList());
  }

  private AllPostsServiceModel mapToServiceModel(Post p) {
    AllPostsServiceModel serviceModel = this.modelMapper.map(p, AllPostsServiceModel.class);
    serviceModel.setCategories(p.getCategories()
      .stream()
      .map(PostCategory::getName)
      .collect(Collectors.toSet()));

    if (p.getLikes().isEmpty()) {
      serviceModel.setLikesSize(0);
    } else {
      serviceModel.setLikesSize(p.getLikes().size());
    }

    if (p.getComments().isEmpty()) {
      serviceModel.setCommentsSize(0);
    } else {
      serviceModel.setCommentsSize(p.getComments().size());
    }

    serviceModel.setArticle(Jsoup.parse(p.getArticle()).text());
    return serviceModel;
  }
}
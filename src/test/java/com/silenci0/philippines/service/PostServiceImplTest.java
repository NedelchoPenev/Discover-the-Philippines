package com.silenci0.philippines.service;

import com.silenci0.philippines.domain.entities.*;
import com.silenci0.philippines.domain.models.service.*;
import com.silenci0.philippines.repository.CommentRepository;
import com.silenci0.philippines.repository.PostRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
@SpringBootTest
public class PostServiceImplTest {

  @Mock
  private PostRepository postRepository;
  @Mock
  private UserService userService;
  @Mock
  private CategoryService categoryService;
  @Mock
  private CommentRepository commentRepository;
  @Mock
  private CloudinaryService cloudinaryService;
  @Mock
  private ModelMapper modelMapper;

  @InjectMocks
  private PostServiceImpl postService;

  private Post post;

  @Before
  public void setUp() throws Exception {
    this.post = new Post(){{
      setId("test_id");
      setTitle("test_title");
      setHeaderImage(mock(Image.class));
      setLikes(new HashSet<>());
      setComments(new HashSet<>());
      setArticle("test article");
      setCategories(new HashSet<>());
      setDatePosted(LocalDateTime.now());
    }};
  }

  @Test
  public void savePost_shouldSavePost() throws IOException {
    PostAddServiceModel serviceModel = new PostAddServiceModel(){{
      setTitle("test_title");
      setHeaderImage(mock(MultipartFile.class));
      setCategories(new HashSet<>(){{
        add("category_id");
      }});
    }};

    Map response = new HashMap(){{
      put("secure_url", "test-url");
      put("public_id", "test-id");
    }};

    when(this.userService.findUserByUsername(any())).thenReturn(mock(User.class));
    when(this.modelMapper.map(any(), any())).thenReturn(this.post);
    when(this.categoryService.findByIdWithoutMap("category_id")).thenReturn(mock(PostCategory.class));
    when(this.cloudinaryService.uploadImageGetFullResponse(any()))
      .thenReturn(response);

    this.postService.savePost(serviceModel, mock(Principal.class));

    verify(this.postRepository, times(1)).saveAndFlush(this.post);
  }

  @Test
  public void findAllPageable_shouldCallFindAll() {
    Pageable pageRequest = PageRequest.of(0, 4);
    List<Post> posts = new ArrayList<>(){{
      add(post);
    }};

    AllPostsServiceModel serviceModel = new AllPostsServiceModel(){{
      setTitle("test_title");
    }};

    Page<Post> pagedPosts = new PageImpl(posts);
    when(this.postRepository.findAll(pageRequest)).thenReturn(pagedPosts);
    when(this.modelMapper.map(any(), any())).thenReturn(serviceModel);

    Page<AllPostsServiceModel> all = this.postService.findAllPageable(pageRequest);

    verify(this.postRepository, times(1)).findAll(pageRequest);
    Assert.assertEquals(this.post.getTitle(), all.getContent().get(0).getTitle());
  }

  @Test
  public void findByKeyword_shouldCallFindByTitleContains() {
    this.post.setComments(new HashSet<>(){{
      add(mock(Comment.class));
    }});
    this.post.setLikes(new HashSet<>(){{
      add(mock(User.class));
    }});

    Pageable pageRequest = PageRequest.of(0, 4);
    List<Post> posts = new ArrayList<>(){{
      add(post);
    }};

    AllPostsServiceModel serviceModel = new AllPostsServiceModel(){{
      setTitle("test_title");
    }};

    Page<Post> pagedPosts = new PageImpl(posts);
    when(this.postRepository.findByTitleContains("test", pageRequest)).thenReturn(pagedPosts);
    when(this.modelMapper.map(any(), any())).thenReturn(serviceModel);

    Page<AllPostsServiceModel> all = this.postService.findByKeyword("test", pageRequest);

    verify(this.postRepository, times(1))
      .findByTitleContains("test", pageRequest);
    Assert.assertEquals(this.post.getTitle(), all.getContent().get(0).getTitle());
  }

  @Test
  public void findTopPosts_shouldReturnCorrectSize() {
    List<Post> posts = new ArrayList<>(){{
      add(post);
      add(post);
      add(post);
      add(post);
      add(post);
      add(post);
    }};

    TopPostServiceModel serviceModel = new TopPostServiceModel(){{
      setTitle("test_title");
      setLikesSize(0);
    }};

    when(this.postRepository.findAll()).thenReturn(posts);
    when(this.modelMapper.map(any(), any())).thenReturn(serviceModel);

    List<TopPostServiceModel> actual = this.postService.findTopPosts();

    Assert.assertEquals(5, actual.size());
  }

  @Test
  public void findPostsByCategoryId_shouldReturnCorrectSize() {
    PostCategory postCategory = new PostCategory(){{
      setPosts(new ArrayList<>(){{
        add(post);
        add(post);
        add(post);
      }});
    }};

    AllPostsServiceModel serviceModel = new AllPostsServiceModel(){{
      setTitle("test_title");
    }};

    when(this.categoryService.findByIdWithoutMap("category_id")).thenReturn(postCategory);
    when(this.modelMapper.map(any(), any())).thenReturn(serviceModel);

    Pageable pageRequest = PageRequest.of(0, 4);
    Page<AllPostsServiceModel> postsPage =
      this.postService.findPostsByCategoryId("category_id", pageRequest);

    Assert.assertEquals(postCategory.getPosts().size(), postsPage.getContent().size());
  }

  @Test
  public void findById_shouldFindCorrect() {
    PostServiceModel postServiceModel = new PostServiceModel(){{
      setId("test_id");
      setTitle("test_title");
      setArticle("test article");
    }};
    when(this.postRepository.findById("test_id")).thenReturn(Optional.ofNullable(this.post));
    when(this.modelMapper.map(any(), any())).thenReturn(postServiceModel);

    PostServiceModel actual = this.postService.findById("test_id");

    Assert.assertEquals(this.post.getId(), actual.getId());
  }

  @Test
  public void findByIdEdit_shouldReturnCorrect() {
    PostEditServiceModel serviceModel = new PostEditServiceModel(){{
      setId("test_id");
      setTitle("test_title");
      setArticle("test article");
    }};

    when(this.postRepository.findById("test_id")).thenReturn(Optional.ofNullable(this.post));
    when(this.modelMapper.map(any(), any())).thenReturn(serviceModel);

    PostEditServiceModel actual = this.postService.findByIdEdit("test_id");

    Assert.assertEquals(this.post.getId(), actual.getId());
  }

  @Test
  public void editPost_shouldEditCorrect() throws IOException {
    Map response = new HashMap(){{
      put("secure_url", "test-url");
      put("public_id", "test-id");
    }};

    this.post.setCategories(new HashSet<>(){{
      add(mock(PostCategory.class));
    }});

    PostEditServiceModel serviceModel = new PostEditServiceModel(){{
      setId("test_id");
      setTitle("test_title");
      setArticle("test article");
      setHeaderImage(mock(MultipartFile.class));
      setCategories(new HashSet<>(){{
        add("category_id");
      }});
    }};

    when(this.postRepository.findById("test_id")).thenReturn(Optional.ofNullable(this.post));
    when(this.userService.findUserByUsername(anyString())).thenReturn(mock(User.class));
    when(this.cloudinaryService.uploadImageGetFullResponse(any()))
      .thenReturn(response);
    when(this.categoryService.findByIdWithoutMap("category_id")).thenReturn(mock(PostCategory.class));

    this.postService.editPost("test_id", serviceModel, "username");

    verify(this.postRepository, times(1)).saveAndFlush(this.post);
  }

  @Test
  public void addLikeToPost_shouldCallSaveAndFlush() {
    when(this.postRepository.findById("test_id")).thenReturn(Optional.ofNullable(this.post));
    when(this.userService.findUserByUsername(anyString())).thenReturn(mock(User.class));

    this.postService.addLikeToPost("test_id", "username");
    verify(this.postRepository, times(1)).saveAndFlush(this.post);
  }

  @Test
  public void addCommentToPost_shouldCallSavePostAndComment() {
    when(this.postRepository.findById("test_id")).thenReturn(Optional.ofNullable(this.post));
    when(this.userService.findUserByUsername(anyString())).thenReturn(mock(User.class));

    this.postService.addCommentToPost("test_id", "test comment", "username");
    verify(this.postRepository, times(1)).saveAndFlush(this.post);
    verify(this.commentRepository, times(1)).save(any());
  }

  @Test
  public void findNewestTakeThree_shouldReturnCorrectSize() {
    List<Post> posts = new ArrayList<>(){{
      add(post);
      add(post);
      add(post);
      add(post);
      add(post);
      add(post);
    }};

    when(this.postRepository.findAll(Sort.by(Sort.Direction.DESC, "datePosted"))).thenReturn(posts);
    when(this.modelMapper.map(any(), any())).thenReturn(mock(PostServiceModel.class));

    List<PostServiceModel> actual = this.postService.findNewestTakeThree();

    Assert.assertEquals(3, actual.size());
  }

  @Test
  public void deleteById_shouldDeleteCorrectPost() {
    this.post.setAuthor(mock(User.class));
    this.post.setCategories(new HashSet<>(){{
      add(mock(PostCategory.class));
    }});
    this.post.setComments(new HashSet<>(){{
      add(new Comment(){{
        setCommenter(mock(User.class));
      }});
    }});

    when(this.postRepository.findById("test_id")).thenReturn(Optional.ofNullable(this.post));

    this.postService.deleteById("test_id");

    ArgumentCaptor<Post> captor = ArgumentCaptor.forClass(Post.class);

    verify(this.postRepository, times(1)).delete(captor.capture());
    Assert.assertEquals(this.post.getId(), captor.getValue().getId());
  }

  @Test
  public void findByEditCommentsId_shouldReturnCorrectPost() {
    PostEditCommentsServiceModel serviceModel = new PostEditCommentsServiceModel(){{
      setId("test_id");
    }};

    when(this.postRepository.findById("test_id")).thenReturn(Optional.ofNullable(this.post));
    when(this.modelMapper.map(any(), any())).thenReturn(serviceModel);

    PostEditCommentsServiceModel actual = this.postService.findByEditCommentsId("test_id");
    Assert.assertEquals(this.post.getId(), actual.getId());
  }

  @Test
  public void deleteComment_shouldDeleteCorrectComment() {
    Comment comment = new Comment(){{
      setCommenter(mock(User.class));
    }};

    when(this.postRepository.findById("test_id")).thenReturn(Optional.ofNullable(this.post));
    when(this.commentRepository.findById("commentId")).thenReturn(Optional.ofNullable(comment));

    this.postService.deleteComment("test_id", "commentId");

    ArgumentCaptor<Post> captor = ArgumentCaptor.forClass(Post.class);

    verify(this.postRepository, times(1)).saveAndFlush(captor.capture());
    Assert.assertEquals(this.post.getId(), captor.getValue().getId());
  }
}
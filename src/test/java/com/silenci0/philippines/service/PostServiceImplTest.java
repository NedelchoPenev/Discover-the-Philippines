package com.silenci0.philippines.service;

import com.silenci0.philippines.domain.entities.Post;
import com.silenci0.philippines.repository.CategoryRepository;
import com.silenci0.philippines.repository.CommentRepository;
import com.silenci0.philippines.repository.PostRepository;
import com.silenci0.philippines.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.Silent.class)
@SpringBootTest
public class PostServiceImplTest {

  @Mock
  private PostRepository postRepository;
  @Mock
  private UserRepository userRepository;
  @Mock
  private CategoryRepository categoryRepository;
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
    }};
  }

  @Test
  public void savePost() {
  }

  @Test
  public void findAllPageable() {
  }

  @Test
  public void findByKeyword() {
  }

  @Test
  public void findTopPosts() {
  }

  @Test
  public void findPostsByCategoryId() {
  }

  @Test
  public void findById() {
  }

  @Test
  public void findByIdEdit() {
  }

  @Test
  public void editPost() {
  }

  @Test
  public void addLikeToPost() {
  }

  @Test
  public void addCommentToPost() {
  }

  @Test
  public void findNewestTakeFour() {
  }

  @Test
  public void findByTitlePageable() {
  }

  @Test
  public void deleteById() {
  }

  @Test
  public void findByEditCommentsId() {
  }

  @Test
  public void deleteComment() {
  }
}
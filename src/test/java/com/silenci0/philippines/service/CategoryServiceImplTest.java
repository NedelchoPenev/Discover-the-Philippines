package com.silenci0.philippines.service;

import com.silenci0.philippines.domain.entities.Post;
import com.silenci0.philippines.domain.entities.PostCategory;
import com.silenci0.philippines.domain.models.service.CategoryServiceModel;
import com.silenci0.philippines.repository.CategoryRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
@SpringBootTest
public class CategoryServiceImplTest {
  @Mock
  private PostCategory testCategory;

  @Mock
  private ModelMapper modelMapper;

  @Mock
  private CategoryRepository mockCategoryRepository;

  @Mock
  private CategoryServiceImpl categoryService;

  @Before
  public void setUp() throws Exception {
    Post post1 = mock(Post.class);
    Post post2 = mock(Post.class);

    this.testCategory = new PostCategory() {{
      setId("category1");
      setName("Test Name 1");
      setPosts(new ArrayList<>(){{
        add(post1);
        add(post2);
      }});
    }};
  }

  @Test
  public void addCategory_ShouldSaveCategory() {
    CategoryServiceModel serviceModel = mock(CategoryServiceModel.class);
    Mockito.when(this.mockCategoryRepository.saveAndFlush(this.testCategory))
      .thenReturn(this.testCategory);

    this.categoryService.addCategory(serviceModel);

    verify(this.categoryService, times(1)).addCategory(serviceModel);
  }

  @Test
  public void findAll() {
  }

  @Test
  public void findTopCategories() {
  }

  @Test
  public void findAllPageable() {
  }

  @Test
  public void deleteById() {
  }

  @Test
  public void findById() {
  }

  @Test
  public void editCategory() {
  }
}
package com.silenci0.philippines.service;

import com.silenci0.philippines.domain.entities.Post;
import com.silenci0.philippines.domain.entities.PostCategory;
import com.silenci0.philippines.domain.models.service.CategoryServiceModel;
import com.silenci0.philippines.domain.models.service.CategoryTopServiceModel;
import com.silenci0.philippines.repository.CategoryRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

  @InjectMocks
  private CategoryServiceImpl categoryService;

  @Mock
  private CategoryServiceModel serviceModel;

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

    this.serviceModel = new CategoryServiceModel(){{
      setId("category1");
      setName("Test Name 1");
    }};
  }

  @Test
  public void addCategory_ShouldSaveCategory() {
    when(this.mockCategoryRepository.saveAndFlush(this.testCategory))
      .thenReturn(this.testCategory);

    this.categoryService.addCategory(serviceModel);

    verify(this.mockCategoryRepository, times(1)).saveAndFlush(any());
  }

  @Test
  public void findAll_ShouldReturnCorrect() {
    CategoryServiceModel serviceModel = mock(CategoryServiceModel.class);
    List<PostCategory> expected = new ArrayList<>() {{
      add(testCategory);
    }};

    when(this.mockCategoryRepository.findAll())
      .thenReturn(expected);
    when(this.modelMapper.map(any(), any()))
      .thenReturn(serviceModel);

    Set<CategoryServiceModel> actual = this.categoryService.findAll();

    Assert.assertEquals(expected.size(), actual.size());
  }

  @Test
  public void findTopCategories_ShouldReturnTopFiveCategories() {
    List<PostCategory> all = new ArrayList<>() {{
      add(testCategory);
      add(new PostCategory() {{
        setId("category2");
        setName("Test Name 2");
        setPosts(new ArrayList<>(){{
          add(mock(Post.class));
        }});
      }});
      add(new PostCategory() {{
        setId("category3");
        setName("Test Name 3");
        setPosts(new ArrayList<>(){{
          add(mock(Post.class));
          add(mock(Post.class));
        }});
      }});
      add(new PostCategory() {{
        setId("category4");
        setName("Test Name 4");
        setPosts(new ArrayList<>(){{
          add(mock(Post.class));
          add(mock(Post.class));
          add(mock(Post.class));
        }});
      }});
      add(new PostCategory() {{
        setId("category5");
        setName("Test Name 5");
        setPosts(new ArrayList<>(){{
          add(mock(Post.class));
          add(mock(Post.class));
          add(mock(Post.class));
          add(mock(Post.class));
        }});
      }});
      add(new PostCategory() {{
        setId("category6");
        setName("Test Name 6");
        setPosts(new ArrayList<>(){{
          add(mock(Post.class));
          add(mock(Post.class));
          add(mock(Post.class));
          add(mock(Post.class));
          add(mock(Post.class));
        }});
      }});
    }};

    CategoryTopServiceModel serviceModel = mock(CategoryTopServiceModel.class);

    when(this.mockCategoryRepository.findAll())
      .thenReturn(all);
    when(this.modelMapper.map(any(), any()))
      .thenReturn(serviceModel);

    List<CategoryTopServiceModel> topCategories = this.categoryService.findTopCategories();
    int expected = 5;

    Assert.assertEquals(expected, topCategories.size());
  }

  @Test
  public void findAllPageable() {
    Page<PostCategory> categories = mock(Page.class);
    when(this.mockCategoryRepository.findAll(isA(Pageable.class))).thenReturn(categories);

    Pageable pageRequest = PageRequest.of(0, 4);
    Page<CategoryServiceModel> pageable = this.categoryService.findAllPageable(pageRequest);
  }

  @Test
  public void deleteById_ShouldDeleteCategory() {
    when(this.mockCategoryRepository.findById("category1"))
      .thenReturn(java.util.Optional.ofNullable(this.testCategory));

    this.categoryService.deleteById("category1");

    verify(this.mockCategoryRepository, times(1)).delete(this.testCategory);
  }

  @Test
  public void findById_ShouldReturnCorrectCategory() {
    when(this.mockCategoryRepository.findById("category1"))
      .thenReturn(java.util.Optional.ofNullable(this.testCategory));
    when(this.modelMapper.map(any(), any()))
      .thenReturn(serviceModel);

    CategoryServiceModel category1 = this.categoryService.findById("category1");

    Assert.assertEquals(this.testCategory.getName(), category1.getName());
  }

  @Test
  public void editCategory_ShouldEditCategory() {
    CategoryServiceModel serviceModelEdit = new CategoryServiceModel(){{
      setName("Test Name 2");
    }};

    when(this.mockCategoryRepository.findById("category1"))
      .thenReturn(java.util.Optional.ofNullable(this.testCategory));

    this.categoryService.editCategory(serviceModelEdit, "category1");

    verify(mockCategoryRepository, times(1)).saveAndFlush(this.testCategory);
  }
}
package com.silenci0.philippines.service;

import com.silenci0.philippines.domain.entities.Post;
import com.silenci0.philippines.domain.entities.PostCategory;
import com.silenci0.philippines.domain.models.service.CategoryServiceModel;
import com.silenci0.philippines.domain.models.service.CategoryTopServiceModel;
import com.silenci0.philippines.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
  private final CategoryRepository categoryRepository;
  private final ModelMapper modelMapper;

  @Autowired
  public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
    this.categoryRepository = categoryRepository;
    this.modelMapper = modelMapper;
  }

  @Override
  public void addCategory(CategoryServiceModel serviceModel) {
    PostCategory category = this.modelMapper.map(serviceModel, PostCategory.class);

    this.categoryRepository.saveAndFlush(category);
  }

  @Override
  public Set<CategoryServiceModel> findAll() {
    return this.categoryRepository.findAll()
      .stream()
      .map(c -> this.modelMapper.map(c, CategoryServiceModel.class))
      .collect(Collectors.toSet());
  }

  @Override
  public List<CategoryTopServiceModel> findTopCategories() {
    List<CategoryTopServiceModel> topCategories = this.categoryRepository.findAll()
      .stream()
      .map(c -> {
        CategoryTopServiceModel serviceModel = this.modelMapper.map(c, CategoryTopServiceModel.class);
        serviceModel.setPostsSize(c.getPosts().isEmpty() ? 0 : c.getPosts().size());

        return serviceModel;
      })
      .sorted((c1, c2) -> c2.getPostsSize().compareTo(c1.getPostsSize()))
      .limit(5)
      .collect(Collectors.toList());

    return topCategories;
  }

  @Override
  public Page<CategoryServiceModel> findAllPageable(Pageable pageable) {
    return this.categoryRepository.findAll(pageable)
      .map(c -> this.modelMapper.map(c, CategoryServiceModel.class));
  }

  @Override
  public void deleteById(String id) {
    PostCategory postCategory = this.categoryRepository.findById(id).orElseThrow();
    List<Post> posts = new ArrayList<>(postCategory.getPosts());
    for (Post post : posts) {
      post.removeCategory(postCategory);
    }
    this.categoryRepository.delete(postCategory);
  }

  @Override
  public CategoryServiceModel findById(String id) {
    return this.modelMapper
      .map(this.categoryRepository.findById(id).orElseThrow(), CategoryServiceModel.class);
  }

  @Override
  public void editCategory(CategoryServiceModel serviceModel, String id) {
    PostCategory postCategory = this.categoryRepository.findById(id).orElseThrow();
    this.modelMapper.map(serviceModel, postCategory);
    postCategory.setId(id);

    this.categoryRepository.saveAndFlush(postCategory);
  }
}

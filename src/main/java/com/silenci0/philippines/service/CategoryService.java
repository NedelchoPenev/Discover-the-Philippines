package com.silenci0.philippines.service;

import com.silenci0.philippines.domain.models.service.CategoryServiceModel;
import com.silenci0.philippines.domain.models.service.CategoryTopServiceModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface CategoryService {

  void addCategory(CategoryServiceModel serviceModel);

  Set<CategoryServiceModel> findAll();

  Page<CategoryServiceModel> findAllPageable(Pageable pageable);

  void deleteById(String id);

  CategoryServiceModel findById(String id);

  void editCategory(CategoryServiceModel serviceModel, String id);

  List<CategoryTopServiceModel> findTopCategories();
}

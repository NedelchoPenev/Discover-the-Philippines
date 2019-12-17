package com.silenci0.philippines.service;

import com.silenci0.philippines.domain.entities.Image;
import com.silenci0.philippines.domain.models.service.AllPicturesServiceModel;
import com.silenci0.philippines.domain.models.service.ImageAddServiceModel;
import com.silenci0.philippines.domain.models.service.ImageEditServiceModel;
import com.silenci0.philippines.domain.models.service.ImageServiceModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ImageService {

  void saveImage(Image image);

  List<ImageServiceModel> findAll();

  Map<String, Set<ImageServiceModel>> findAllByCategory(String category);

  List<ImageServiceModel> findAllFromCategoryByName(String category, String name);

  void saveImages(ImageAddServiceModel imageServiceModel, Principal principal) throws IOException;

  Page<AllPicturesServiceModel> findAllPageable(Pageable pageable);

  String deleteById(String id) throws IOException;

  ImageEditServiceModel findById(String id);

  Image findByIdWithoutMap(String id);

  void editImage(String id, ImageEditServiceModel serviceModel);
}

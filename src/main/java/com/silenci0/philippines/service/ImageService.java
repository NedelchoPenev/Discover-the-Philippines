package com.silenci0.philippines.service;

import com.silenci0.philippines.domain.models.service.ImageServiceModel;

import java.util.List;

public interface ImageService {

  List<ImageServiceModel> findAll();

  List<ImageServiceModel> findByProvince(String province);

  List<ImageServiceModel> findByPlace(String place);
}

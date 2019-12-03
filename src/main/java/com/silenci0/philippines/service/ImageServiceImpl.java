package com.silenci0.philippines.service;

import com.silenci0.philippines.domain.models.service.ImageServiceModel;
import com.silenci0.philippines.repository.ImageRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImageServiceImpl implements ImageService{
  private final ImageRepository imageRepository;
  private final ModelMapper modelMapper;

  @Autowired
  public ImageServiceImpl(ImageRepository imageRepository, ModelMapper modelMapper) {
    this.imageRepository = imageRepository;
    this.modelMapper = modelMapper;
  }

  @Override
  public List<ImageServiceModel> findAll() {
    return this.imageRepository.findAll()
      .stream()
      .map(i -> this.modelMapper.map(i, ImageServiceModel.class))
      .collect(Collectors.toList());
  }

  @Override
  public List<ImageServiceModel> findByProvince(String province) {
    return null;
  }

  @Override
  public List<ImageServiceModel> findByPlace(String place) {
    return null;
  }
}

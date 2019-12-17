package com.silenci0.philippines.service;

import com.silenci0.philippines.domain.entities.Image;
import com.silenci0.philippines.domain.models.service.AllPicturesServiceModel;
import com.silenci0.philippines.domain.models.service.ImageAddServiceModel;
import com.silenci0.philippines.domain.models.service.ImageEditServiceModel;
import com.silenci0.philippines.domain.models.service.ImageServiceModel;
import com.silenci0.philippines.repository.ImageRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ImageServiceImpl implements ImageService {
  private static final String INCORRECT_ID = "There is no image with that id!";

  private final ImageRepository imageRepository;
  private final ModelMapper modelMapper;
  private final UserService userService;
  private final CloudinaryService cloudinaryService;

  @Autowired
  public ImageServiceImpl(ImageRepository imageRepository,
                          ModelMapper modelMapper,
                          UserService userService,
                          CloudinaryService cloudinaryService) {
    this.imageRepository = imageRepository;
    this.modelMapper = modelMapper;
    this.userService = userService;
    this.cloudinaryService = cloudinaryService;
  }

  @Override
  public void saveImage(Image image){
    this.imageRepository.saveAndFlush(image);
  }

  @Override
  public List<ImageServiceModel> findAll() {
    return this.imageRepository.findAll()
      .stream()
      .sorted((i1, i2) -> i2.getUploadDate().compareTo(i1.getUploadDate()))
      .map(i -> this.modelMapper.map(i, ImageServiceModel.class))
      .collect(Collectors.toList());
  }

  @Override
  public Map<String, Set<ImageServiceModel>> findAllByCategory(String category) {
    List<ImageServiceModel> imageList = this.findAll();
    Map<String, Set<ImageServiceModel>> imageMap;

    switch (category) {
      case "province":
        imageMap = imageList.stream()
          .filter(i -> i.getProvince() != null)
          .collect(Collectors.groupingBy(ImageServiceModel::getProvince,
            Collectors.mapping(i -> i, Collectors.toSet())
          ));
        break;
      case "place":
        imageMap = imageList.stream()
          .filter(i -> i.getPlace() != null)
          .collect(Collectors.groupingBy(ImageServiceModel::getPlace,
            Collectors.mapping(i -> i, Collectors.toSet())
          ));
        break;
      case "uploader":
        imageMap = imageList.stream()
          .filter(i -> i.getUploaderUsername() != null)
          .collect(Collectors.groupingBy(ImageServiceModel::getUploaderUsername,
            Collectors.mapping(i -> i, Collectors.toSet())
          ));
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + category);
    }

    return imageMap;
  }

  @Override
  public List<ImageServiceModel> findAllFromCategoryByName(String category, String name) {
    List<Image> result;

    switch (category) {
      case "province":
        result = this.imageRepository.findByProvince(name);
        break;
      case "place":
        result = this.imageRepository.findByPlace(name);
        break;
      case "uploader":
        result = this.imageRepository.findByUploaderUsername(name);
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + category);
    }

    return result
      .stream()
      .map(i -> this.modelMapper.map(i, ImageServiceModel.class))
      .collect(Collectors.toList());
  }

  @Override
  public void saveImages(ImageAddServiceModel imageServiceModel, Principal principal) throws IOException {
    for (MultipartFile imageUp : imageServiceModel.getImages()) {
      Image image = this.modelMapper.map(imageServiceModel, Image.class);
      image.setPlace(imageServiceModel.getPlace() == null ?
        imageServiceModel.getPlaceNew() :
        imageServiceModel.getPlace());

      Map response = this.cloudinaryService.uploadImageGetFullResponse(imageUp);
      image.setUrl(response.get("secure_url").toString());
      image.setPublic_id((response.get("public_id").toString()));
      image.setUploadDate(LocalDateTime.now());
      image.setUploader(this.userService.findUserByUsername(principal.getName()));
      this.imageRepository.saveAndFlush(image);
    }
  }

  @Override
  public Page<AllPicturesServiceModel> findAllPageable(Pageable pageable) {
    return this.imageRepository.findAll(pageable)
      .map(i -> this.modelMapper.map(i, AllPicturesServiceModel.class));
  }

  @Override
  public String deleteById(String id) throws IOException {
    Image image = this.imageRepository.findById(id).orElseThrow();
    this.imageRepository.deleteById(id);
    return this.cloudinaryService.deleteImage(image.getPublic_id()).get("result").toString();
  }

  @Override
  public ImageEditServiceModel findById(String id) {
    return this.modelMapper.map(this.imageRepository.findById(id).orElseThrow(() ->
        new IllegalArgumentException(INCORRECT_ID)),
      ImageEditServiceModel.class);
  }

  @Override
  public Image findByIdWithoutMap(String id) {
    return this.imageRepository.findById(id).orElseThrow(() ->
        new IllegalArgumentException(INCORRECT_ID));
  }

  @Override
  public void editImage(String id, ImageEditServiceModel serviceModel) {
    Image image = this.imageRepository.findById(id).orElseThrow(() ->
      new IllegalArgumentException(INCORRECT_ID));
    this.modelMapper.map(serviceModel, image);

    image.setPlace(serviceModel.getPlace() == null ?
      serviceModel.getPlaceNew() :
      serviceModel.getPlace());

    this.imageRepository.saveAndFlush(image);
  }
}

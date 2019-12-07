package com.silenci0.philippines.service;

import com.silenci0.philippines.domain.entities.Image;
import com.silenci0.philippines.domain.entities.ThingsToDo;
import com.silenci0.philippines.domain.entities.User;
import com.silenci0.philippines.domain.models.service.*;
import com.silenci0.philippines.repository.ImageRepository;
import com.silenci0.philippines.repository.ThingsToDoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ThingsToDoServiceImpl implements ThingsToDoService {

  private static final String NAME_NOT_FOUND = "Name not found!";
  private static final String ID_NOT_FOUND = "Entity with that id doesn't exist!";
  private static final String MAIN_IMAGE = "You cannot delete the main image!";
  private final ThingsToDoRepository thingsToDoRepository;
  private final ImageRepository imageRepository;
  private final ModelMapper modelMapper;
  private final CloudinaryService cloudinaryService;
  private final UserService userService;

  @Autowired
  public ThingsToDoServiceImpl(ThingsToDoRepository thingsToDoRepository,
                               ImageRepository imageRepository,
                               ModelMapper modelMapper,
                               CloudinaryService cloudinaryService,
                               UserService userService) {
    this.thingsToDoRepository = thingsToDoRepository;
    this.imageRepository = imageRepository;
    this.modelMapper = modelMapper;
    this.cloudinaryService = cloudinaryService;
    this.userService = userService;
  }

  @Override
  public void saveThing(ThingsToDoAddServiceModel thingsToDoAddServiceModel, Principal principal) throws IOException {
    ThingsToDo thingsToDo = this.modelMapper.map(thingsToDoAddServiceModel, ThingsToDo.class);
    UserServiceModel userByUserName = this.userService.findUserByUserName(principal.getName());

    List<Image> imagesUrls = new ArrayList<>();
    for (MultipartFile image : thingsToDoAddServiceModel.getImages()) {
      Image imageDB = mapImage(thingsToDo, userByUserName, image);
      imagesUrls.add(imageDB);
      this.imageRepository.save(imageDB);
    }
    thingsToDo.setImagesUrls(imagesUrls);
    thingsToDo.setMainImageUrl(imagesUrls.get(0).getUrl());
    this.thingsToDoRepository.saveAndFlush(thingsToDo);
  }

  @Override
  public Page<ThingsToDoMainImageServiceModel> findAllPageable(Pageable pageable) {
    return this.thingsToDoRepository.findAll(pageable)
      .map(thing -> this.modelMapper.map(thing, ThingsToDoMainImageServiceModel.class));
  }

  @Override
  public List<String> findAllProvinces() {
    return this.thingsToDoRepository.getAllProvinces()
      .stream()
      .sorted(Comparator.comparing(String::toString))
      .collect(Collectors.toList());
  }

  @Override
  public Page<ThingsToDoMainImageServiceModel> findByProvincePageable(String province, Pageable pageable) {
    return this.thingsToDoRepository.findByProvince(province, pageable)
      .map(pr -> this.modelMapper.map(pr, ThingsToDoMainImageServiceModel.class));
  }

  @Override
  public Page<ThingsToDoMainImageServiceModel> findByNamePageable(String name, Pageable pageable) {
    return this.thingsToDoRepository.findByNameContains(name, pageable)
      .map(pr -> this.modelMapper.map(pr, ThingsToDoMainImageServiceModel.class));
  }

  @Override
  public void deleteById(String id) {
    this.thingsToDoRepository.deleteById(id);
  }

  @Override
  public ThingsToDoDeleteImageServiceModel findByIdEdit(String id) {
    ThingsToDo thingsToDo =
      this.thingsToDoRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException(NAME_NOT_FOUND));

    ThingsToDoDeleteImageServiceModel thingsToDoServiceModel =
      this.modelMapper.map(thingsToDo, ThingsToDoDeleteImageServiceModel.class);

    List<ImageDeleteServiceModel> imageDeleteServiceModels = new ArrayList<>();
    for (Image images : thingsToDo.getImagesUrls()) {
      ImageDeleteServiceModel deleteServiceModel =
        new ImageDeleteServiceModel(images.getId(), images.getUrl());
      imageDeleteServiceModels.add(deleteServiceModel);
    }
    thingsToDoServiceModel.setImagesUrls(imageDeleteServiceModels);

    return thingsToDoServiceModel;
  }

  @Override
  public String deleteImageById(String thingToDoId, String imageId) {
    ThingsToDo thingsToDo = this.thingsToDoRepository.findById(thingToDoId)
      .orElseThrow(() -> new IllegalArgumentException(ID_NOT_FOUND));
    Image image = this.imageRepository.findById(imageId)
      .orElseThrow(() -> new IllegalArgumentException(ID_NOT_FOUND));

    if (thingsToDo.getMainImageUrl().equals(image.getUrl())){
      throw new IllegalArgumentException(MAIN_IMAGE);
    }

    thingsToDo.getImagesUrls().remove(image);
    this.thingsToDoRepository.saveAndFlush(thingsToDo);
    return imageId;
  }

  @Override
  public void editThingToDo(String id, ThingsToDoEditServiceModel serviceModel, Principal principal) throws IOException {
    ThingsToDo thingsToDo =
      this.thingsToDoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(ID_NOT_FOUND));
    UserServiceModel userByUsername = this.userService.findUserByUserName(principal.getName());

    for (MultipartFile image : serviceModel.getImages()) {
      if (!image.isEmpty()) {
        Image imageDB = mapImage(thingsToDo, userByUsername, image);
        thingsToDo.getImagesUrls().add(imageDB);
        this.imageRepository.save(imageDB);
      }
    }

    String mainImageUrl = thingsToDo.getMainImageUrl();
    this.modelMapper.map(serviceModel, thingsToDo);
    if (serviceModel.getMainImageUrl() == null){
      thingsToDo.setMainImageUrl(mainImageUrl);
    }

    this.thingsToDoRepository.saveAndFlush(thingsToDo);
  }

  @Override
  public ThingsToDoServiceModel findById(String id) {
    ThingsToDo thingsToDo = this.thingsToDoRepository.findById(id)
      .orElseThrow(() -> new IllegalArgumentException(ID_NOT_FOUND));

    ThingsToDoServiceModel serviceModel = this.modelMapper.map(thingsToDo, ThingsToDoServiceModel.class);

    serviceModel.setImagesUrls(thingsToDo.getImagesUrls()
      .stream()
      .map(Image::getUrl)
      .collect(Collectors.toList()));

    return serviceModel;
  }

  private Image mapImage(ThingsToDo thingsToDo, UserServiceModel userByUsername, MultipartFile image) throws IOException {
    Image imageDB = new Image();
    Map imageMap = this.cloudinaryService.uploadImageGetFullResponse(image);
    imageDB.setUrl(imageMap.get("secure_url").toString());
    imageDB.setPublic_id(imageMap.get("public_id").toString());
    imageDB.setProvince(thingsToDo.getProvince());
    imageDB.setUploadDate(LocalDateTime.now());
    imageDB.setUploader(this.modelMapper.map(userByUsername, User.class));

    return imageDB;
  }
}
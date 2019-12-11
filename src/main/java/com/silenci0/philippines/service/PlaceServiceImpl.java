package com.silenci0.philippines.service;

import com.silenci0.philippines.domain.entities.Image;
import com.silenci0.philippines.domain.entities.Place;
import com.silenci0.philippines.domain.entities.User;
import com.silenci0.philippines.domain.models.service.AllPlacesServiceModel;
import com.silenci0.philippines.domain.models.service.PlaceServiceModel;
import com.silenci0.philippines.domain.models.service.UserServiceModel;
import com.silenci0.philippines.repository.PlaceRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PlaceServiceImpl implements PlaceService {
  private static final String PLACE_NOT_FOUND = "Place not found!";

  private final PlaceRepository placeRepository;
  private final UserService userService;
  private final ModelMapper modelMapper;
  private final CloudinaryService cloudinaryService;

  @Autowired
  public PlaceServiceImpl(PlaceRepository placeRepository,
                          UserService userService,
                          ModelMapper modelMapper,
                          CloudinaryService cloudinaryService) {
    this.placeRepository = placeRepository;
    this.userService = userService;
    this.modelMapper = modelMapper;
    this.cloudinaryService = cloudinaryService;
  }

  @Override
  public void savePlace(PlaceServiceModel placeServiceModel, Principal principal) throws IOException {
    Place place = this.modelMapper.map(placeServiceModel, Place.class);
    mapImage(placeServiceModel, principal, place);
    place.setDateAdded(LocalDateTime.now());
    this.placeRepository.saveAndFlush(place);
  }

  @Override
  public List<AllPlacesServiceModel> findAll() {
    return this.placeRepository.findAll()
        .stream()
        .map(place -> this.modelMapper.map(place, AllPlacesServiceModel.class))
        .collect(Collectors.toList());
  }

  @Override
  public List<AllPlacesServiceModel> findNewestTakeFour() {
    return this.placeRepository.findAll()
      .stream()
      .sorted((p1, p2) -> p2.getDateAdded().compareTo(p1.getDateAdded()))
      .map(place -> this.modelMapper.map(place, AllPlacesServiceModel.class))
      .limit(4)
      .collect(Collectors.toList());
  }

  @Override
  public PlaceServiceModel findById(String id) {
    Place place = this.placeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(PLACE_NOT_FOUND));

    return this.modelMapper.map(place, PlaceServiceModel.class);
  }

  @Override
  public Page<AllPlacesServiceModel> findAllPageable(Pageable pageable) {
    return this.placeRepository.findAll(pageable)
        .map(place -> this.modelMapper.map(place, AllPlacesServiceModel.class));
  }

  @Override
  public Page<AllPlacesServiceModel> findByNamePlace(String name, Pageable pageable) {
    return this.placeRepository.findByNameStartingWith(name, pageable)
        .map(place -> this.modelMapper.map(place, AllPlacesServiceModel.class));
  }

  @Override
  public void deleteById(String id) {
    this.placeRepository.deleteById(id);
  }

  @Override
  public void editPlace(String id, PlaceServiceModel placeServiceModel, Principal principal) throws IOException {
    Place place = this.placeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(PLACE_NOT_FOUND));

    this.modelMapper.map(placeServiceModel, place);

    mapImage(placeServiceModel, principal, place);

    this.placeRepository.saveAndFlush(place);
  }

  private void mapImage(PlaceServiceModel placeServiceModel, Principal principal, Place place) throws IOException {
    if (!placeServiceModel.getHeaderImage().isEmpty()) {
      Image image = new Image();
      UserServiceModel userByUserName = this.userService.findUserByUserName(principal.getName());
      Map map = this.cloudinaryService.uploadImageGetFullResponse(placeServiceModel.getHeaderImage());
      image.setUrl(map.get("secure_url").toString());
      image.setPublic_id(map.get("public_id").toString());
      image.setPlace(placeServiceModel.getName());
      image.setProvince(placeServiceModel.getProvince());
      image.setUploadDate(LocalDateTime.now());
      image.setUploader(this.modelMapper.map(userByUserName, User.class));
      place.setHeaderImage(image);
    }
  }
}

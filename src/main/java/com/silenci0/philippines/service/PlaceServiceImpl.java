package com.silenci0.philippines.service;

import com.silenci0.philippines.domain.entities.Place;
import com.silenci0.philippines.domain.models.service.AllPlacesServiceModel;
import com.silenci0.philippines.domain.models.service.PlaceServiceModel;
import com.silenci0.philippines.repository.PlaceRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlaceServiceImpl implements PlaceService {
  private static final String PLACE_NOT_FOUND = "Place not found!";

  private final PlaceRepository placeRepository;
  private final ModelMapper modelMapper;
  private final CloudinaryService cloudinaryService;

  @Autowired
  public PlaceServiceImpl(PlaceRepository placeRepository, ModelMapper modelMapper, CloudinaryService cloudinaryService) {
    this.placeRepository = placeRepository;
    this.modelMapper = modelMapper;
    this.cloudinaryService = cloudinaryService;
  }

  @Override
  public void savePlace(PlaceServiceModel placeServiceModel) throws IOException {
    Place place = this.modelMapper.map(placeServiceModel, Place.class);
    if (!placeServiceModel.getHeaderImage().isEmpty()) {
      place.setHeaderImageUrl(this.cloudinaryService.uploadImage(placeServiceModel.getHeaderImage()));
    }
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
  public PlaceServiceModel findById(String id) {
    Place place = this.placeRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException(PLACE_NOT_FOUND));

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
  public void editPlace(String id, PlaceServiceModel placeServiceModel) throws IOException {
    Place place = this.placeRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException(PLACE_NOT_FOUND));

    if (!placeServiceModel.getHeaderImage().isEmpty()) {
      place.setHeaderImageUrl(this.cloudinaryService
        .uploadImage(placeServiceModel.getHeaderImage()));
    }

    this.modelMapper.map(placeServiceModel, place);

    this.placeRepository.saveAndFlush(place);
  }
}

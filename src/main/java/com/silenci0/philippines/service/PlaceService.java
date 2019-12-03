package com.silenci0.philippines.service;

import com.silenci0.philippines.domain.models.service.AllPlacesServiceModel;
import com.silenci0.philippines.domain.models.service.PlaceServiceModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface PlaceService {

  void savePlace(PlaceServiceModel placeServiceModel) throws IOException;

  List<AllPlacesServiceModel> findAll();

  PlaceServiceModel findById(String id);

  Page<AllPlacesServiceModel> findAllPageable(Pageable pageable);

  Page<AllPlacesServiceModel> findByNamePlace(String name, Pageable pageable);

  void deleteById(String id);

  void editPlace(String id, PlaceServiceModel placeServiceModel) throws IOException;
}

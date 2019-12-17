package com.silenci0.philippines.service;

import com.silenci0.philippines.domain.models.service.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

public interface ThingsToDoService {

  void saveThing(ThingsToDoAddServiceModel thingsToDoAddServiceModel, Principal principal) throws IOException;

  Page<ThingsToDoMainImageServiceModel> findAllPageable(Pageable pageable);

  List<String> findAllProvinces();

  Page<ThingsToDoMainImageServiceModel> findByProvincePageable(String province, Pageable pageable);

  Page<ThingsToDoMainImageServiceModel> findByNamePageable(String name, Pageable pageable);

  void deleteById(String id);

  ThingsToDoDeleteImageServiceModel findByIdEdit(String id);

  String deleteImageById(String id, String name);

  void editThingToDo(String id, ThingsToDoEditServiceModel serviceModel, Principal principal) throws IOException;

  ThingsToDoServiceModel findById(String id);

  List<ThingsToDoMainImageServiceModel> findNewestTakeFour();

}

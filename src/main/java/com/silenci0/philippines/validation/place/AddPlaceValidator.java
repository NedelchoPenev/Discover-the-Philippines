package com.silenci0.philippines.validation.place;

import com.silenci0.philippines.domain.entities.Place;
import com.silenci0.philippines.domain.models.binding.PlaceBindingModel;
import com.silenci0.philippines.repository.PlaceRepository;
import com.silenci0.philippines.validation.ValidationConstants;
import com.silenci0.philippines.validation.annotation.Validatory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Validatory
public class AddPlaceValidator implements Validator {

  private final PlaceRepository placeRepository;

  @Autowired
  public AddPlaceValidator(PlaceRepository placeRepository) {
    this.placeRepository = placeRepository;
  }

  @Override
  public boolean supports(Class<?> aClass) {
    return PlaceBindingModel.class.equals(aClass);
  }

  @Override
  public void validate(Object o, Errors errors) {
    PlaceBindingModel placeBindingModel = (PlaceBindingModel) o;
    Optional<Place> place = this.placeRepository.findByName(placeBindingModel.getName());

    if (place.isPresent()) {
      errors.rejectValue(
          "name",
          String.format(ValidationConstants.NAME_ALREADY_EXISTS, placeBindingModel.getName()),
          String.format(ValidationConstants.NAME_ALREADY_EXISTS, placeBindingModel.getName())
      );
    }

    if (placeBindingModel.getName() == null ||
        placeBindingModel.getName().length() < 3 ||
        placeBindingModel.getName().length() > 20) {
      errors.rejectValue(
          "name",
          ValidationConstants.PLACE_NAME_LENGTH,
          ValidationConstants.PLACE_NAME_LENGTH
      );
    }

    if (placeBindingModel.getHeaderImage().isEmpty()) {
      errors.rejectValue(
          "headerImage",
          ValidationConstants.EMPTY_IMAGE,
          ValidationConstants.EMPTY_IMAGE
      );
    }

    if (placeBindingModel.getProvince() == null) {
      errors.rejectValue(
          "province",
          ValidationConstants.EMPTY_PROVINCE,
          ValidationConstants.EMPTY_PROVINCE
      );
    }

    if (placeBindingModel.getArticle().isEmpty()) {
      errors.rejectValue(
          "article",
          ValidationConstants.EMPTY_ARTICLE,
          ValidationConstants.EMPTY_ARTICLE
      );
    }

    if (placeBindingModel.getInfo() == null ||
        placeBindingModel.getInfo().length() < 50 ||
        placeBindingModel.getInfo().length() > 255) {
      errors.rejectValue(
          "info",
          ValidationConstants.PLACE_INFO_LENGTH,
          ValidationConstants.PLACE_INFO_LENGTH
      );
    }
  }
}


package com.silenci0.philippines.validation.thingsToDo;

import com.silenci0.philippines.domain.entities.ThingsToDo;
import com.silenci0.philippines.domain.models.binding.ThingsToDoBindingModel;
import com.silenci0.philippines.repository.ThingsToDoRepository;
import com.silenci0.philippines.validation.ValidationConstants;
import com.silenci0.philippines.validation.annotation.Validatory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Validatory
public class AddThingToDoValidator implements Validator {

  private final ThingsToDoRepository thingsToDoRepository;

  @Autowired
  public AddThingToDoValidator(ThingsToDoRepository thingsToDoRepository) {
    this.thingsToDoRepository = thingsToDoRepository;
  }

  @Override
  public boolean supports(Class<?> aClass) {
    return ThingsToDoBindingModel.class.equals(aClass);
  }

  @Override
  public void validate(Object o, Errors errors) {
    ThingsToDoBindingModel thingsToDoBindingModel = (ThingsToDoBindingModel) o;
    Optional<ThingsToDo> thingToDo = this.thingsToDoRepository.findByName(thingsToDoBindingModel.getName());

    if (thingToDo.isPresent()) {
      errors.rejectValue(
          "name",
          String.format(ValidationConstants.NAME_ALREADY_EXISTS, thingsToDoBindingModel.getName()),
          String.format(ValidationConstants.NAME_ALREADY_EXISTS, thingsToDoBindingModel.getName())
      );
    }

    if (thingsToDoBindingModel.getName() == null ||
      thingsToDoBindingModel.getName().length() < 3) {
      errors.rejectValue(
          "name",
          ValidationConstants.THINGTODO_NAME_LENGTH,
          ValidationConstants.THINGTODO_NAME_LENGTH
      );
    }

    if (thingsToDoBindingModel.getImages().isEmpty()) {
      errors.rejectValue(
          "images",
          ValidationConstants.EMPTY_IMAGE,
          ValidationConstants.EMPTY_IMAGE
      );
    }

    if (thingsToDoBindingModel.getProvince() == null) {
      errors.rejectValue(
          "province",
          ValidationConstants.EMPTY_PROVINCE,
          ValidationConstants.EMPTY_PROVINCE
      );
    }

    if (thingsToDoBindingModel.getOverview().equals("") ||
      thingsToDoBindingModel.getOverview().length() < 30) {
      errors.rejectValue(
          "overview",
          ValidationConstants.THINGTODO_OVERVIEW_LENGTH,
          ValidationConstants.THINGTODO_OVERVIEW_LENGTH
      );
    }
  }
}


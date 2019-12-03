package com.silenci0.philippines.validation.thingsToDo;

import com.silenci0.philippines.domain.models.binding.ThingsToDoEditBindingModel;
import com.silenci0.philippines.validation.ValidationConstants;
import com.silenci0.philippines.validation.annotation.Validatory;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Validatory
public class EditThingToDoValidator implements Validator {

  @Override
  public boolean supports(Class<?> aClass) {
    return ThingsToDoEditBindingModel.class.equals(aClass);
  }

  @Override
  public void validate(Object o, Errors errors) {
    ThingsToDoEditBindingModel thingsToDoBindingModel = (ThingsToDoEditBindingModel) o;

    if (thingsToDoBindingModel.getName() == null ||
      thingsToDoBindingModel.getName().length() < 3 ||
      thingsToDoBindingModel.getName().length() > 20) {
      errors.rejectValue(
        "name",
        ValidationConstants.PLACE_NAME_LENGTH,
        ValidationConstants.PLACE_NAME_LENGTH
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

package com.silenci0.philippines.validation.image;

import com.silenci0.philippines.domain.models.binding.ImageBindingModel;
import com.silenci0.philippines.validation.ValidationConstants;
import com.silenci0.philippines.validation.annotation.Validatory;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Validatory
public class AddImageValidator implements Validator {

  @Override
  public boolean supports(Class<?> aClass) {
    return ImageBindingModel.class.equals(aClass);
  }

  @Override
  public void validate(Object o, Errors errors) {
    ImageBindingModel imageBindingModel = (ImageBindingModel) o;

    if (imageBindingModel.getPlace() == null && imageBindingModel.getPlaceNew() == null){
      errors.rejectValue("place",
        ValidationConstants.FILL_PLACE,
        ValidationConstants.FILL_PLACE);
    }

    if (imageBindingModel.getProvince() == null) {
      errors.rejectValue(
        "province",
        ValidationConstants.EMPTY_PROVINCE,
        ValidationConstants.EMPTY_PROVINCE
      );
    }
  }
}

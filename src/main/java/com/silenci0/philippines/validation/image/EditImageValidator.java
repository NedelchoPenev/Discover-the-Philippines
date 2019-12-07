package com.silenci0.philippines.validation.image;

import com.silenci0.philippines.domain.models.binding.ImageEditBindingModel;
import com.silenci0.philippines.validation.ValidationConstants;
import com.silenci0.philippines.validation.annotation.Validatory;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Validatory
public class EditImageValidator implements Validator {

  @Override
  public boolean supports(Class<?> aClass) {
    return ImageEditBindingModel.class.equals(aClass);
  }

  @Override
  public void validate(Object o, Errors errors) {
    ImageEditBindingModel imageBindingModel = (ImageEditBindingModel) o;

    if (imageBindingModel.getPlace() == null && imageBindingModel.getPlaceNew().equals("")){
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

package com.silenci0.philippines.validation.post;

import com.silenci0.philippines.domain.models.binding.PostEditBindingModel;
import com.silenci0.philippines.validation.ValidationConstants;
import com.silenci0.philippines.validation.annotation.Validatory;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Validatory
public class PostEditValidator implements Validator {

  @Override
  public boolean supports(Class<?> aClass) {
    return PostEditBindingModel.class.equals(aClass);
  }

  @Override
  public void validate(Object o, Errors errors) {
    PostEditBindingModel postBindingModel = (PostEditBindingModel) o;

    if (postBindingModel.getTitle() == null ||
      postBindingModel.getTitle().length() < 5) {
      errors.rejectValue(
        "title",
        ValidationConstants.POST_TITLE_LENGTH,
        ValidationConstants.POST_TITLE_LENGTH
      );
    }

    if (postBindingModel.getArticle().isEmpty() ||
      postBindingModel.getArticle().length() < 50) {
      errors.rejectValue(
        "article",
        ValidationConstants.EMPTY_ARTICLE,
        ValidationConstants.EMPTY_ARTICLE
      );
    }

    if (postBindingModel.getCategories()== null ||
      postBindingModel.getCategories().isEmpty()) {
      errors.rejectValue(
        "categories",
        ValidationConstants.EMPTY_CATEGORY,
        ValidationConstants.EMPTY_CATEGORY
      );
    }
  }
}
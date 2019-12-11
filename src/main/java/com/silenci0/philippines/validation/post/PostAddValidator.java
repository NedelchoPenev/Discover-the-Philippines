package com.silenci0.philippines.validation.post;

import com.silenci0.philippines.domain.models.binding.PostBindingModel;
import com.silenci0.philippines.validation.ValidationConstants;
import com.silenci0.philippines.validation.annotation.Validatory;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Validatory
public class PostAddValidator implements Validator {
  @Override
  public boolean supports(Class<?> aClass) {
    return PostBindingModel.class.equals(aClass);
  }

  @Override
  public void validate(Object o, Errors errors) {
    PostBindingModel postBindingModel = (PostBindingModel) o;

    if (postBindingModel.getTitle() == null ||
      postBindingModel.getTitle().length() < 5) {
      errors.rejectValue(
        "title",
        ValidationConstants.POST_TITLE_LENGTH,
        ValidationConstants.POST_TITLE_LENGTH
      );
    }

    if (postBindingModel.getHeaderImage().isEmpty()) {
      errors.rejectValue(
        "headerImage",
        ValidationConstants.EMPTY_IMAGE,
        ValidationConstants.EMPTY_IMAGE
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


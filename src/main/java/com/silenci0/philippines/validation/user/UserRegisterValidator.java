package com.silenci0.philippines.validation.user;

import com.silenci0.philippines.domain.models.binding.RegisterUserBindingModel;
import com.silenci0.philippines.repository.UserRepository;
import com.silenci0.philippines.validation.ValidationConstants;
import com.silenci0.philippines.validation.annotation.Validatory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Validatory
public class UserRegisterValidator implements Validator {

  private final UserRepository userRepository;

  @Autowired
  public UserRegisterValidator(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public boolean supports(Class<?> aClass) {
    return RegisterUserBindingModel.class.equals(aClass);
  }

  @Override
  public void validate(Object o, Errors errors) {
    RegisterUserBindingModel userRegisterBindingModel = (RegisterUserBindingModel) o;

    if (this.userRepository.findByUsername(userRegisterBindingModel.getUsername()).isPresent()) {
      errors.rejectValue(
          "username",
          String.format(ValidationConstants.USERNAME_ALREADY_EXISTS, userRegisterBindingModel.getUsername()),
          String.format(ValidationConstants.USERNAME_ALREADY_EXISTS, userRegisterBindingModel.getUsername())
      );
    }

    if (userRegisterBindingModel.getUsername() == null ||
        userRegisterBindingModel.getUsername().length() < 3 ||
        userRegisterBindingModel.getUsername().length() > 20) {
      errors.rejectValue(
          "username",
          ValidationConstants.USERNAME_LENGTH,
          ValidationConstants.USERNAME_LENGTH
      );
    }

    if (
        !userRegisterBindingModel.getPassword().equals(userRegisterBindingModel.getConfirmPassword())) {
      errors.rejectValue(
          "password",
          ValidationConstants.PASSWORDS_DO_NOT_MATCH,
          ValidationConstants.PASSWORDS_DO_NOT_MATCH
      );
    }

    if (this.userRepository.findByEmail(userRegisterBindingModel.getEmail()).isPresent()) {
      errors.rejectValue(
          "email",
          String.format(ValidationConstants.EMAIL_ALREADY_EXISTS,
              userRegisterBindingModel.getEmail()),
          String.format(ValidationConstants.EMAIL_ALREADY_EXISTS,
              userRegisterBindingModel.getEmail())
      );
    }

    Pattern pattern = Pattern.compile(ValidationConstants.EMAIL_REGEX);
    Matcher matcher = pattern.matcher(userRegisterBindingModel.getEmail());
    if (!matcher.matches()){
      errors.rejectValue(
          "email",
          String.format(ValidationConstants.REAL_EMAIL, userRegisterBindingModel.getEmail()),
          String.format(ValidationConstants.REAL_EMAIL, userRegisterBindingModel.getEmail())
      );
    }
  }
}

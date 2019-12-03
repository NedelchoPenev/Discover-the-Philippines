package com.silenci0.philippines.validation.user;

import com.silenci0.philippines.domain.entities.User;
import com.silenci0.philippines.domain.models.binding.EditProfileBindingModel;
import com.silenci0.philippines.repository.UserRepository;
import com.silenci0.philippines.validation.ValidationConstants;
import com.silenci0.philippines.validation.annotation.Validatory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Validatory
public class UserEditValidator implements Validator {

  private final UserRepository userRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  public UserEditValidator(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.userRepository = userRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  @Override
  public boolean supports(Class<?> aClass) {
    return EditProfileBindingModel.class.equals(aClass);
  }

  @Override
  public void validate(Object o, Errors errors) {
    EditProfileBindingModel userEditBindingModel = (EditProfileBindingModel) o;

    User user = this.userRepository.findByUsername(userEditBindingModel.getUsername()).orElse(null);

    if (user == null) {
      errors.rejectValue(
          "username",
          ValidationConstants.USERNAME_CHANGE_NOT_ALLOWED,
          ValidationConstants.USERNAME_CHANGE_NOT_ALLOWED
      );
    } else {
      if (!this.bCryptPasswordEncoder.matches(userEditBindingModel.getCurrentPassword(), user.getPassword())) {
        errors.rejectValue(
            "currentPassword",
            ValidationConstants.WRONG_PASSWORD,
            ValidationConstants.WRONG_PASSWORD
        );
      }
      if (!user.getEmail().equals(userEditBindingModel.getEmail()) && this.userRepository.findByEmail(userEditBindingModel.getEmail()).isPresent()) {
        errors.rejectValue(
            "email",
            String.format(ValidationConstants.EMAIL_ALREADY_EXISTS, userEditBindingModel.getEmail()),
            String.format(ValidationConstants.EMAIL_ALREADY_EXISTS, userEditBindingModel.getEmail())
        );
      }

      if (userEditBindingModel.getCurrentPassword().equals("")) {
        errors.rejectValue(
            "currentPassword",
            ValidationConstants.ENTER_PASSWORD,
            ValidationConstants.ENTER_PASSWORD
        );
      } else if (!userEditBindingModel.getNewPassword().equals("") && !userEditBindingModel.getNewPassword().equals(userEditBindingModel.getConfirmPassword())) {
        errors.rejectValue(
            "newPassword",
            ValidationConstants.PASSWORDS_DO_NOT_MATCH,
            ValidationConstants.PASSWORDS_DO_NOT_MATCH
        );
      }
    }

    Pattern pattern = Pattern.compile(ValidationConstants.EMAIL_REGEX);
    Matcher matcher = pattern.matcher(userEditBindingModel.getEmail());
    if (!matcher.matches()){
      errors.rejectValue(
          "email",
          String.format(ValidationConstants.REAL_EMAIL, userEditBindingModel.getEmail()),
          String.format(ValidationConstants.REAL_EMAIL, userEditBindingModel.getEmail())
      );
    }
  }
}

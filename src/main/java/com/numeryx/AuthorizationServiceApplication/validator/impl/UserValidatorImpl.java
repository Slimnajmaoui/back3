package com.numeryx.AuthorizationServiceApplication.validator.impl;

import com.numeryx.AuthorizationServiceApplication.dto.BaseUserDto;
import com.numeryx.AuthorizationServiceApplication.dto.UserDto;
import com.numeryx.AuthorizationServiceApplication.dto.request.CreateUserRequest;
import com.numeryx.AuthorizationServiceApplication.dto.request.UpdateUserProfileRequest;
import com.numeryx.AuthorizationServiceApplication.exception.Reason;
import com.numeryx.AuthorizationServiceApplication.exception.ValidationException;
import com.numeryx.AuthorizationServiceApplication.repository.UserRepository;
import com.numeryx.AuthorizationServiceApplication.validator.UserValidator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.numeryx.AuthorizationServiceApplication.utilities.Constant.*;

@Component("personValidator")
public class UserValidatorImpl implements UserValidator {


    private final UserRepository userRepository;

    public UserValidatorImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void beforeUpdate(UserDto user) {
        List<Reason> reasons = new ArrayList<>(validateBaseUserDTO(user));
        if (!reasons.isEmpty())
            throw new ValidationException(reasons);
    }

    @Override
    public void beforeSave(CreateUserRequest user) {
        List<Reason> reasons = new ArrayList<>(validateBaseUserDTO(user));
        if (userRepository.existsByUsernameIgnoreCase(user.getUsername())) {
            reasons.add(new Reason<>(ValidationException.ReasonCode.USERNAME_ALREADY_EXIST, USERNAME_ALREADY_EXIST));
        }
        if (!reasons.isEmpty()) {
            throw new ValidationException(reasons);
        }
    }

    @Override
    public void beforeUpdateProfile(UpdateUserProfileRequest user) {
        List<Reason> reasons = new ArrayList<>();
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            reasons.add(new Reason<>(ValidationException.ReasonCode.IS_EMPTY, USER_NAME_EMPTY));
        }
        if (!user.getUsername().matches(emailRegex)) {
            reasons.add(new Reason(ValidationException.ReasonCode.INVALID_EMAIL_USER, EMAIL_EMPTY));
        }

        if (user.getFirstname() == null || user.getFirstname().isEmpty()) {
            reasons.add(new Reason<>(ValidationException.ReasonCode.IS_EMPTY, USER_FIRST_NAME_EMPTY));
        }

        if (user.getLastname() == null || user.getLastname().isEmpty()) {
            reasons.add(new Reason<>(ValidationException.ReasonCode.IS_EMPTY, USER_LAST_NAME_EMPTY));
        }

        if (user.getJob() == null || user.getJob().isEmpty()) {
            reasons.add(new Reason<>(ValidationException.ReasonCode.IS_EMPTY, JOB_EMPTY));
        }

        if (user.getPhone() == null || user.getPhone().isEmpty()) {
            reasons.add(new Reason<>(ValidationException.ReasonCode.IS_EMPTY, PHONE_EMPTY));
        }

        if (user.getPhone() != null && !user.getPhone().matches(phoneRegex)) {
            reasons.add(new Reason(ValidationException.ReasonCode.INVALID, PHONE_INVALID));
        }

        if (!reasons.isEmpty())
            throw new ValidationException(reasons);
    }

    private List<Reason> validateBaseUserDTO(BaseUserDto user) {
        List<Reason> reasons = new ArrayList<>();
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            reasons.add(new Reason<>(ValidationException.ReasonCode.IS_EMPTY, USER_NAME_EMPTY));
        }
        if (!user.getUsername().matches(emailRegex)) {
            reasons.add(new Reason(ValidationException.ReasonCode.INVALID_EMAIL_USER, EMAIL_EMPTY));
        }

        if (user.getFirstname() == null || user.getFirstname().isEmpty()) {
            reasons.add(new Reason<>(ValidationException.ReasonCode.IS_EMPTY, USER_FIRST_NAME_EMPTY));
        }

        if (user.getLastname() == null || user.getLastname().isEmpty()) {
            reasons.add(new Reason<>(ValidationException.ReasonCode.IS_EMPTY, USER_LAST_NAME_EMPTY));
        }

        if (user.getJob() == null || user.getJob().isEmpty()) {
            reasons.add(new Reason<>(ValidationException.ReasonCode.IS_EMPTY, JOB_EMPTY));
        }

        if (user.getPhone() == null || user.getPhone().isEmpty()) {
            reasons.add(new Reason<>(ValidationException.ReasonCode.IS_EMPTY, PHONE_EMPTY));
        }

        if (!user.getPhone().matches(phoneRegex)) {
            reasons.add(new Reason(ValidationException.ReasonCode.INVALID, PHONE_INVALID));
        }

        if (user.getRole() == null) {
            reasons.add(new Reason<>(ValidationException.ReasonCode.IS_EMPTY, ROLE_EMPTY));
        }

        return reasons;
    }
}

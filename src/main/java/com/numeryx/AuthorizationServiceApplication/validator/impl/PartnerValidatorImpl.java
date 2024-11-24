package com.numeryx.AuthorizationServiceApplication.validator.impl;

import com.numeryx.AuthorizationServiceApplication.dto.PartnerAccountDTO;
import com.numeryx.AuthorizationServiceApplication.dto.request.CreateUserRequest;
import com.numeryx.AuthorizationServiceApplication.exception.Reason;
import com.numeryx.AuthorizationServiceApplication.exception.UserException;
import com.numeryx.AuthorizationServiceApplication.exception.ValidationException;
import com.numeryx.AuthorizationServiceApplication.model.User;
import com.numeryx.AuthorizationServiceApplication.repository.UserRepository;
import com.numeryx.AuthorizationServiceApplication.validator.PartnerValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.numeryx.AuthorizationServiceApplication.utilities.Constant.*;

@Component("partnerValidator")
public class PartnerValidatorImpl implements PartnerValidator {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void beforeSave(CreateUserRequest partner) {
        List<Reason> reasons = new ArrayList<>();
        /**
         * Check if user with input email already exists
         */
        User user = userRepository.findByUsernameIgnoreCase(partner.getUsername());
        if (user != null) {
            reasons.add(new Reason<>(UserException.ReasonCode.USERNAME_ALREADY_EXIST, USER_ALREADY_EXISTS));
        }
        /**
         * Check if email format is null
         */
        if (partner.getUsername() == null) {
            reasons.add(new Reason(ValidationException.ReasonCode.IS_NULL, REQUIRED_EMAIL));
        }
        /**
         * Check if email format is correct
         */
        if (partner.getUsername() != null && !partner.getUsername().matches(emailRegex)) {
            reasons.add(new Reason(ValidationException.ReasonCode.INVALID_EMAIL, INVALID_EMAIL_USER_EXCEPTION));
        }

        if (!reasons.isEmpty())
            throw new ValidationException(reasons);
    }

    @Override
    public void beforeUpdate(PartnerAccountDTO partnerAccountDTO) {
        List<Reason> reasons = new ArrayList<>();
        if (partnerAccountDTO.getUsername() == null || partnerAccountDTO.getUsername().isEmpty()) {
            reasons.add(new Reason<>(ValidationException.ReasonCode.IS_EMPTY, USER_NAME_EMPTY));
        }
        if (!partnerAccountDTO.getUsername().matches(emailRegex)) {
            reasons.add(new Reason(ValidationException.ReasonCode.INVALID_EMAIL_USER, EMAIL_EMPTY));
        }

        if (partnerAccountDTO.getFirstname() == null || partnerAccountDTO.getFirstname().isEmpty()) {
            reasons.add(new Reason<>(ValidationException.ReasonCode.IS_EMPTY, USER_FIRST_NAME_EMPTY));
        }

        if (partnerAccountDTO.getLastname() == null || partnerAccountDTO.getLastname().isEmpty()) {
            reasons.add(new Reason<>(ValidationException.ReasonCode.IS_EMPTY, USER_LAST_NAME_EMPTY));
        }

        if (partnerAccountDTO.getJob() == null || partnerAccountDTO.getJob().isEmpty()) {
            reasons.add(new Reason<>(ValidationException.ReasonCode.IS_EMPTY, JOB_EMPTY));
        }

        if (partnerAccountDTO.getPhone() == null || partnerAccountDTO.getPhone().isEmpty()) {
            reasons.add(new Reason<>(ValidationException.ReasonCode.IS_EMPTY, PHONE_EMPTY));
        }

        if (partnerAccountDTO.getPhone() != null && !partnerAccountDTO.getPhone().matches(phoneRegex)) {
            reasons.add(new Reason(ValidationException.ReasonCode.INVALID, PHONE_INVALID));
        }

        if (!reasons.isEmpty())
            throw new ValidationException(reasons);
    }
}
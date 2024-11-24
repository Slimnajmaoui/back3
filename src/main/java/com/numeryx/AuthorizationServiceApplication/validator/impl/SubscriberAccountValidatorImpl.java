package com.numeryx.AuthorizationServiceApplication.validator.impl;

import com.numeryx.AuthorizationServiceApplication.dto.SubscriberAccountDTO;
import com.numeryx.AuthorizationServiceApplication.dto.SubscriberDTO;
import com.numeryx.AuthorizationServiceApplication.dto.request.CreateSubscriberAccountDTO;
import com.numeryx.AuthorizationServiceApplication.exception.EntityNotFoundException;
import com.numeryx.AuthorizationServiceApplication.exception.Reason;
import com.numeryx.AuthorizationServiceApplication.exception.UserException;
import com.numeryx.AuthorizationServiceApplication.exception.ValidationException;
import com.numeryx.AuthorizationServiceApplication.model.Subscriber;
import com.numeryx.AuthorizationServiceApplication.model.SubscriberAccount;
import com.numeryx.AuthorizationServiceApplication.model.User;
import com.numeryx.AuthorizationServiceApplication.repository.ISubscriberAccountRepository;
import com.numeryx.AuthorizationServiceApplication.repository.ISubscriberRepository;
import com.numeryx.AuthorizationServiceApplication.repository.UserRepository;
import com.numeryx.AuthorizationServiceApplication.validator.SubscriberAccountValidator;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

import static com.numeryx.AuthorizationServiceApplication.utilities.Constant.*;

@Component("subscriberAccounValidator")
public class SubscriberAccountValidatorImpl implements SubscriberAccountValidator {

    private final ISubscriberRepository subscriberRepository;
    private final UserRepository userRepository;
    private final ISubscriberAccountRepository subscriberAccountRepository;

    public SubscriberAccountValidatorImpl(ISubscriberRepository subscriberRepository,
                                          UserRepository userRepository,
                                          ISubscriberAccountRepository subscriberAccountRepository) {
        this.subscriberRepository = subscriberRepository;
        this.userRepository = userRepository;
        this.subscriberAccountRepository = subscriberAccountRepository;
    }

    @Override
    public void beforeSave(CreateSubscriberAccountDTO dto) {

        List<Reason> reasons = new ArrayList<>();
        /**
         * Check if user with input email already exists
         */
        User user = userRepository.findByUsernameIgnoreCase(dto.getEmail());
        if (user != null) {
            reasons.add(new Reason<>(UserException.ReasonCode.USERNAME_ALREADY_EXIST, USER_ALREADY_EXISTS));
        }
        /**
         * Check if subscriber with input id exists
         */
        Subscriber subscriber = subscriberRepository.findById(dto.getIdSubscriber()).orElse(null);
        if (subscriber == null) {
            reasons.add(new Reason<>(EntityNotFoundException.ReasonCode.SUBSCRIBER_NOT_FOUND, SUBSCRIBER_NOT_FOUND_EXCEPTION));
        } else {
            /**
             * Check if email format is null
             */
            if (dto.getEmail() == null) {
                reasons.add(new Reason(ValidationException.ReasonCode.IS_NULL, REQUIRED_EMAIL));
            }
            /**
             * Check if email format is correct
             */
            if (dto.getEmail() != null && !dto.getEmail().matches(emailRegex)) {
                reasons.add(new Reason(ValidationException.ReasonCode.INVALID_EMAIL, INVALID_EMAIL_USER_EXCEPTION));
            }
            /**
             * Check if user with input email already exists for subscriber with input id
             */
            SubscriberAccount subscriberAccount = subscriberAccountRepository.findByUsernameIgnoreCaseAndSubscriber_Id(dto.getEmail(), dto.getIdSubscriber());
            if (subscriberAccount != null) {
                reasons.add(new Reason<>(UserException.ReasonCode.USER_ALREADY_EXISTS_FOR_SUBSCRIBER, USER_ALREADY_EXISTS_FOR_SUBSCRIBER));
            }
        }
        if (!reasons.isEmpty())
            throw new ValidationException(reasons);
    }

    @Override
    public void beforeUpdate(SubscriberAccountDTO dto) {
        List<Reason> reasons = new ArrayList<>();
        if (dto.getUsername() == null || dto.getUsername().isEmpty()) {
            reasons.add(new Reason<>(ValidationException.ReasonCode.IS_EMPTY, USER_NAME_EMPTY));
        }
        if (!dto.getUsername().matches(emailRegex)) {
            reasons.add(new Reason(ValidationException.ReasonCode.INVALID_EMAIL_USER, EMAIL_EMPTY));
        }

        if (dto.getFirstname() == null || dto.getFirstname().isEmpty()) {
            reasons.add(new Reason<>(ValidationException.ReasonCode.IS_EMPTY, USER_FIRST_NAME_EMPTY));
        }

        if (dto.getLastname() == null || dto.getLastname().isEmpty()) {
            reasons.add(new Reason<>(ValidationException.ReasonCode.IS_EMPTY, USER_LAST_NAME_EMPTY));
        }

        if (dto.getJob() == null || dto.getJob().isEmpty()) {
            reasons.add(new Reason<>(ValidationException.ReasonCode.IS_EMPTY, JOB_EMPTY));
        }

        if (dto.getPhone() == null || dto.getPhone().isEmpty()) {
            reasons.add(new Reason<>(ValidationException.ReasonCode.IS_EMPTY, PHONE_EMPTY));
        }

        if (dto.getPhone() != null && !dto.getPhone().matches(phoneRegex)) {
            reasons.add(new Reason(ValidationException.ReasonCode.INVALID, PHONE_INVALID));
        }

        if (!reasons.isEmpty())
            throw new ValidationException(reasons);
    }

}

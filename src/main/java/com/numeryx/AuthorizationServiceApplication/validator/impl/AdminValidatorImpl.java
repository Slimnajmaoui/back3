package com.numeryx.AuthorizationServiceApplication.validator.impl;

import com.numeryx.AuthorizationServiceApplication.dto.AdminDTO;
import com.numeryx.AuthorizationServiceApplication.dto.BaseUserDto;
import com.numeryx.AuthorizationServiceApplication.dto.request.CreateAdminDTO;
import com.numeryx.AuthorizationServiceApplication.enumeration.FunctionEnum;
import com.numeryx.AuthorizationServiceApplication.exception.Reason;
import com.numeryx.AuthorizationServiceApplication.exception.ValidationException;
import com.numeryx.AuthorizationServiceApplication.repository.UserRepository;
import com.numeryx.AuthorizationServiceApplication.validator.AdminValidator;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import static com.numeryx.AuthorizationServiceApplication.utilities.Constant.*;

@Component("adminValidator")
public class AdminValidatorImpl implements AdminValidator {


    private final UserRepository userRepository;

    public AdminValidatorImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public void beforeUpdate(AdminDTO updateRequest) {

    }

    @Override
    public void beforeSave(CreateAdminDTO createRequest) {
        List<Reason> reasons = new ArrayList<>(validateBaseUserDTO(createRequest));

        if (userRepository.existsByUsernameIgnoreCase(createRequest.getUsername())) {
            reasons.add(new Reason<>(ValidationException.ReasonCode.USERNAME_ALREADY_EXIST, USERNAME_ALREADY_EXIST));
        }
        if (createRequest.getFunction() == null
                || !(FunctionEnum.FINANCIER.equals(createRequest.getFunction()) || FunctionEnum.PROJECT_DIRECTOR.equals(createRequest.getFunction())
        || FunctionEnum.PARTNER.equals(createRequest.getFunction()))) {
            reasons.add(new Reason<>(ValidationException.ReasonCode.WRONG_FUNCTION, WRONG_FUNCTION));
        }
        if (!reasons.isEmpty()) {
            throw new ValidationException(reasons);
        }
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
//
//        if (user.getRole() == null) {
//            reasons.add(new Reason<>(ValidationException.ReasonCode.IS_EMPTY, ROLE_EMPTY));
//        }

        return reasons;
    }
}

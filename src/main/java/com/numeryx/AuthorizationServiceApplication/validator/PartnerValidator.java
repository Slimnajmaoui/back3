package com.numeryx.AuthorizationServiceApplication.validator;

import com.numeryx.AuthorizationServiceApplication.dto.PartnerAccountDTO;
import com.numeryx.AuthorizationServiceApplication.dto.request.CreateUserRequest;

public interface PartnerValidator extends IGenericValidator<CreateUserRequest, PartnerAccountDTO> {
}

package com.numeryx.AuthorizationServiceApplication.validator;

import com.numeryx.AuthorizationServiceApplication.dto.SubscriberDTO;
import com.numeryx.AuthorizationServiceApplication.dto.request.CreateSubscriberDTO;

import javax.validation.Validator;

public interface SubscriberValidator extends IGenericValidator<CreateSubscriberDTO, SubscriberDTO> {
}

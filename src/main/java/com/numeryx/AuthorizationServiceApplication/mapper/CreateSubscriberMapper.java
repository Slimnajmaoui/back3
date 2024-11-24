package com.numeryx.AuthorizationServiceApplication.mapper;

import com.numeryx.AuthorizationServiceApplication.dto.request.CreateSubscriberDTO;
import com.numeryx.AuthorizationServiceApplication.model.Subscriber;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN, uses = {AddressMapper.class})
public interface CreateSubscriberMapper extends EntityMapper<CreateSubscriberDTO, Subscriber> {
}

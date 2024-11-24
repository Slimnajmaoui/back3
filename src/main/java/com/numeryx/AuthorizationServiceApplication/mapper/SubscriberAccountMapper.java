package com.numeryx.AuthorizationServiceApplication.mapper;

import com.numeryx.AuthorizationServiceApplication.dto.SubscriberAccountDTO;
import com.numeryx.AuthorizationServiceApplication.dto.UserDto;
import com.numeryx.AuthorizationServiceApplication.model.SubscriberAccount;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN, uses = {UserDto.class})
public interface SubscriberAccountMapper extends EntityMapper<SubscriberAccountDTO, SubscriberAccount> {

}

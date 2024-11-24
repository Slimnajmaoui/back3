package com.numeryx.AuthorizationServiceApplication.mapper;

import com.numeryx.AuthorizationServiceApplication.dto.AddressDto;
import com.numeryx.AuthorizationServiceApplication.dto.SubscriberDTO;
import com.numeryx.AuthorizationServiceApplication.model.Subscriber;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {AddressDto.class})
public interface SubscriberMapper extends EntityMapper<SubscriberDTO, Subscriber>{
}

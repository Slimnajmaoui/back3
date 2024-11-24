package com.numeryx.AuthorizationServiceApplication.service;

import com.numeryx.AuthorizationServiceApplication.dto.SubscriberDTO;
import com.numeryx.AuthorizationServiceApplication.dto.request.CreateSubscriberDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ISubscriberService {
    Page<SubscriberDTO>findAll(String searchValue, Pageable pageable);
    SubscriberDTO lock(Long id, Boolean active);
    SubscriberDTO create(CreateSubscriberDTO subscriberDTO);
    SubscriberDTO update(SubscriberDTO subscriberDTO);
    Page<SubscriberDTO> findBySubscriberName(Pageable pageable, String subscriberName);
    SubscriberDTO findById(Long id);
}

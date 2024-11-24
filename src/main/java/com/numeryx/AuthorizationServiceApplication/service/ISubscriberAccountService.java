package com.numeryx.AuthorizationServiceApplication.service;

import com.numeryx.AuthorizationServiceApplication.dto.SubscriberAccountDTO;
import com.numeryx.AuthorizationServiceApplication.dto.request.CreateSubscriberAccountDTO;
import com.numeryx.AuthorizationServiceApplication.model.SubscriberAccount;

import java.util.List;

public interface ISubscriberAccountService extends IUserAccountManagement {
    SubscriberAccountDTO addUserByEmail(CreateSubscriberAccountDTO createSubscriberAccountDTO);
    List<SubscriberAccount> findAllSubscriber();
    SubscriberAccountDTO addSlaveAccounts(SubscriberAccountDTO subscriberAccountDTO);
    List<SubscriberAccountDTO> findUserAccountsByAdmin(String searchValue);
    void deleteSubscriberAccountUser(Long id);
    SubscriberAccountDTO update (SubscriberAccountDTO subscriberAccountDTO);
}

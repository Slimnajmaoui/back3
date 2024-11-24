package com.numeryx.AuthorizationServiceApplication.service;

import com.numeryx.AuthorizationServiceApplication.dto.PartnerAccountDTO;
import com.numeryx.AuthorizationServiceApplication.dto.request.CreateUserRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IPartnerAccountService extends IUserAccountManagement{

    PartnerAccountDTO addPartnerAccount(CreateUserRequest createUserRequest);
    Page<PartnerAccountDTO> searchPartnerAccounts(String keySearch, Long idAdmin, Pageable pageable);
    void deletePartnerAccount(Long idPartnerAccount) throws Exception;
    PartnerAccountDTO updatePartnerAccount(PartnerAccountDTO partnerAccountDTO);
    PartnerAccountDTO lockPartnerAccount(Long idAccount);
}

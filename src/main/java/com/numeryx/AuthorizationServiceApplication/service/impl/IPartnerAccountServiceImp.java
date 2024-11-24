package com.numeryx.AuthorizationServiceApplication.service.impl;

import com.numeryx.AuthorizationServiceApplication.dto.PartnerAccountDTO;
import com.numeryx.AuthorizationServiceApplication.dto.request.CreateUserRequest;
import com.numeryx.AuthorizationServiceApplication.enumeration.RoleEnum;
import com.numeryx.AuthorizationServiceApplication.exception.Reason;
import com.numeryx.AuthorizationServiceApplication.exception.UserException;
import com.numeryx.AuthorizationServiceApplication.mapper.PartnerAccountMapper;
import com.numeryx.AuthorizationServiceApplication.model.Admin;
import com.numeryx.AuthorizationServiceApplication.model.PartnerAccount;
import com.numeryx.AuthorizationServiceApplication.repository.IAdminRepository;
import com.numeryx.AuthorizationServiceApplication.repository.IPartnerAccountRepository;
import com.numeryx.AuthorizationServiceApplication.service.IPartnerAccountService;
import com.numeryx.AuthorizationServiceApplication.utilities.Constant;
import com.numeryx.AuthorizationServiceApplication.validator.PartnerValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

import static com.numeryx.AuthorizationServiceApplication.exception.UserException.ReasonCode.USER_NOT_FOUND;

@Service
public class IPartnerAccountServiceImp implements IPartnerAccountService {

    @Autowired
    private AdminServiceImpl adminService;

    @Autowired
    private IPartnerAccountRepository iPartnerAccountRepository;

    @Autowired
    private PartnerAccountMapper partnerAccountMapper;

    @Autowired
    private PartnerValidator partnerValidator;

    @Autowired
    private IAdminRepository iAdminRepository;

    @Override
    public PartnerAccountDTO addPartnerAccount(CreateUserRequest createUserRequest) {
        partnerValidator.beforeSave(createUserRequest);
        PartnerAccount partnerAccount = new PartnerAccount();
        partnerAccount.setUsername(createUserRequest.getUsername());
        partnerAccount.setFirstname(createUserRequest.getFirstname());
        partnerAccount.setLastname(createUserRequest.getLastname());
        partnerAccount.setJob(createUserRequest.getJob());
        partnerAccount.setPhone(createUserRequest.getPhone());
        partnerAccount.setRole(RoleEnum.ROLE_USER_PROVIDER);
        partnerAccount.setEnabled(false);
        partnerAccount.setPartnerAccount(getMe());
        PartnerAccount savedPartnerAccount =  iPartnerAccountRepository.save(partnerAccount);
        adminService.sendUserSmsAndMailNotification(savedPartnerAccount, "Partner");
        return partnerAccountMapper.toDto(savedPartnerAccount);
    }

    @Override
    public Page<PartnerAccountDTO> searchPartnerAccounts(String keySearch, Long idAdmin, Pageable pageable) {
            if (keySearch != "" && keySearch != null ) {
                return iPartnerAccountRepository.searchPartnerAccounts(keySearch, idAdmin, pageable).map(partnerAccount -> partnerAccountMapper.toDto(partnerAccount));
        }
        return iPartnerAccountRepository.getPartnerAccounts(idAdmin, pageable).map(partnerAccount -> partnerAccountMapper.toDto(partnerAccount));

    }

    @Override
    public void deletePartnerAccount(Long idPartnerAccount) throws Exception {
        Optional<PartnerAccount> partnerAccount = iPartnerAccountRepository.findById(idPartnerAccount);
        if (partnerAccount.isPresent()) {
            iPartnerAccountRepository.deleteById(idPartnerAccount);
        } else {
            throw new Exception("user not found");
        }
    }

    @Override
    public PartnerAccountDTO updatePartnerAccount(PartnerAccountDTO partnerAccountDTO) {
        partnerValidator.beforeUpdate(partnerAccountDTO);
        Optional<PartnerAccount> partnerAccount = iPartnerAccountRepository.findById(partnerAccountDTO.getId());
        if (partnerAccount.isPresent()) {
            partnerAccount.get().setUsername(partnerAccountDTO.getUsername());
            partnerAccount.get().setFirstname(partnerAccountDTO.getFirstname());
            partnerAccount.get().setLastname(partnerAccountDTO.getLastname());
            partnerAccount.get().setPhone(partnerAccountDTO.getPhone());
            partnerAccount.get().setJob(partnerAccountDTO.getJob());
            iPartnerAccountRepository.save(partnerAccount.get());
            return partnerAccountMapper.toDto(partnerAccount.get());
        } else {
            throw new UserException(Collections.singletonList(new Reason(USER_NOT_FOUND, Constant.USER_NOT_FOUND_EXCEPTION)));
        }
    }

    @Override
    public PartnerAccountDTO lockPartnerAccount(Long idAccount) {
        Optional<PartnerAccount> partnerAccount = iPartnerAccountRepository.findById(idAccount);
        if (partnerAccount.isPresent()) {
            if (partnerAccount.get().isEnabled()) {
                partnerAccount.get().setEnabled(false);
            } else {
                partnerAccount.get().setEnabled(true);
            }
           PartnerAccount savedPartner = iPartnerAccountRepository.save(partnerAccount.get());
            return partnerAccountMapper.toDto(savedPartner);
        } else {
            throw new UserException(Collections.singletonList(new Reason(USER_NOT_FOUND, Constant.USER_NOT_FOUND_EXCEPTION)));
        }
    }

    public Admin getMe() {
        Admin userChecked = iAdminRepository.findByUsernameIgnoreCase(
                SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        if (userChecked == null) {
            throw new UserException(Collections.singletonList(new Reason(USER_NOT_FOUND, Constant.USER_NOT_FOUND_EXCEPTION)));
        }
        if (userChecked.getAttempts() != 0) {
            userChecked.setAttempts(0);
            userChecked.setEnabled(true);
            return iAdminRepository.save(userChecked);
        } else {
            return userChecked;
        }
    }


}

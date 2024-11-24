package com.numeryx.AuthorizationServiceApplication.service.impl;

import com.numeryx.AuthorizationServiceApplication.client.NotificationFeignClient;
import com.numeryx.AuthorizationServiceApplication.dto.SubscriberAccountDTO;
import com.numeryx.AuthorizationServiceApplication.dto.request.CreateSubscriberAccountDTO;
import com.numeryx.AuthorizationServiceApplication.enumeration.RoleEnum;
import com.numeryx.AuthorizationServiceApplication.exception.Reason;
import com.numeryx.AuthorizationServiceApplication.exception.UserException;
import com.numeryx.AuthorizationServiceApplication.mapper.SubscriberAccountMapper;
import com.numeryx.AuthorizationServiceApplication.model.Subscriber;
import com.numeryx.AuthorizationServiceApplication.model.SubscriberAccount;
import com.numeryx.AuthorizationServiceApplication.repository.ISubscriberAccountRepository;
import com.numeryx.AuthorizationServiceApplication.repository.ISubscriberRepository;
import com.numeryx.AuthorizationServiceApplication.repository.TokenRepository;
import com.numeryx.AuthorizationServiceApplication.repository.UserRepository;
import com.numeryx.AuthorizationServiceApplication.service.ISubscriberAccountService;
import com.numeryx.AuthorizationServiceApplication.utilities.Constant;
import com.numeryx.AuthorizationServiceApplication.validator.SubscriberAccountValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static com.numeryx.AuthorizationServiceApplication.exception.UserException.ReasonCode.USER_NOT_FOUND;

@Service("subscriberAccountComponent")
public class SubscriberAccountServiceImpl extends UserAccountManagementImpl implements ISubscriberAccountService {
    private final Logger log = LoggerFactory.getLogger(SubscriberAccountServiceImpl.class);
    @Autowired
    private ISubscriberRepository subscriberRepository;
    @Autowired
    private ISubscriberAccountRepository subscriberAccountRepository;
    @Autowired
    private SubscriberAccountMapper subscriberAccountMapper;
    @Autowired
    private SubscriberAccountValidator subscriberAccountValidator;

    public SubscriberAccountServiceImpl(UserRepository userRepository,
                                        LoginAttemptService loginAttemptService,
                                        TokenRepository tokenRepository,
                                        NotificationFeignClient notificationFeignClient) {
        super(userRepository, loginAttemptService, tokenRepository, notificationFeignClient);
    }

    @Override
    public SubscriberAccountDTO addUserByEmail(CreateSubscriberAccountDTO dto) {
        log.debug("Attempting to add an account with email " + dto.getEmail() + " to the subscriber with id " + dto.getIdSubscriber());
        subscriberAccountValidator.beforeSave(dto);
        log.debug("Validation of DTO done");
        Subscriber subscriber = subscriberRepository.findById(dto.getIdSubscriber()).orElse(null);
        SubscriberAccount subscriberAccount = new SubscriberAccount();
            subscriberAccount.setUsername(dto.getEmail());
            // TODO: Role will be set dynamically later
            subscriberAccount.setRole(RoleEnum.ROLE_ADMIN_SUBSCRIBER);
            subscriberAccount.setSubscriber(subscriber);
            subscriberAccount.setEnabled(false);
            subscriberAccount.getSubscriber().setHasAccount(true);
            // TODO: check if admin or delegate
            log.debug("Request to save subscriber account {}", subscriberAccount);
            subscriberAccountRepository.save(subscriberAccount);
            // send set password and verification code emails
            log.debug("send notification mail and activation code {}", subscriberAccount);
            this.sendSubscriberSmsAndMailNotification(subscriberAccount);
        return subscriberAccountMapper.toDto(subscriberAccount);
    }

    @Override
    public List<SubscriberAccount> findAllSubscriber() {
        return subscriberAccountRepository.findAll();
    }

    @Override
    public SubscriberAccountDTO addSlaveAccounts(SubscriberAccountDTO dto) {

        CreateSubscriberAccountDTO dto1 = new CreateSubscriberAccountDTO(dto.getUsername(),getMe().getSubscriber().getId());
        subscriberAccountValidator.beforeSave(dto1);
        log.debug("Validation of DTO done");

        SubscriberAccount subscriberAccount = new SubscriberAccount();

            subscriberAccount.setUsername(dto.getUsername());
            subscriberAccount.setFirstname(dto.getFirstname());
            subscriberAccount.setLastname(dto.getLastname());
            subscriberAccount.setJob(dto.getJob());
            subscriberAccount.setPhone(dto.getPhone());
            subscriberAccount.setRole(RoleEnum.ROLE_USER_SUBSCRIBER);
            subscriberAccount.setEnabled(false);
            subscriberAccount.setSubscriberAccount(getMe());
            subscriberAccount.setSubscriber(getMe().getSubscriber());

        subscriberAccountRepository.save(subscriberAccount);

        this.sendSubscriberSmsAndMailNotification(subscriberAccount);
        return subscriberAccountMapper.toDto(subscriberAccount);
    }

    @Override
    public List<SubscriberAccountDTO> findUserAccountsByAdmin(String searchValue){

        if (searchValue != null && !searchValue.isEmpty()) {
                List<SubscriberAccount> userList = subscriberAccountRepository.searchSubscriberAccountUsers(searchValue,getMe().getId());
                return subscriberAccountMapper.toDto(userList);
        }
        List<SubscriberAccount> usersList = subscriberAccountRepository.findSubscriberAccountsBySubscriberAccount(getMe(),Sort.by("firstname").ascending());
        return subscriberAccountMapper.toDto(usersList);
    }

    public SubscriberAccount getMe() {
        SubscriberAccount userChecked = subscriberAccountRepository.findByUsernameIgnoreCase(
                SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        if (userChecked == null) {
            throw new UserException(Collections.singletonList(new Reason(USER_NOT_FOUND, Constant.USER_NOT_FOUND_EXCEPTION)));
        }
        if (userChecked.getAttempts() != 0) {
            userChecked.setAttempts(0);
            userChecked.setEnabled(true);
            return subscriberAccountRepository.save(userChecked);
        } else {
            return userChecked;
        }
    }

    @Override
    public void deleteSubscriberAccountUser(Long id){
        subscriberAccountRepository.deleteById(id);
    }

    @Override
        public SubscriberAccountDTO update (SubscriberAccountDTO subscriberAccountDTO){
        log.debug("attempting to update subscriber {}", subscriberAccountDTO);
        subscriberAccountValidator.beforeUpdate(subscriberAccountDTO);
        log.debug("subscriber content is valid, attempting to update entity");
        SubscriberAccount subscriberAccount = subscriberAccountRepository.getOne(subscriberAccountDTO.getId());
        subscriberAccount.setFirstname(subscriberAccountDTO.getFirstname());
        subscriberAccount.setLastname(subscriberAccountDTO.getLastname());
        subscriberAccount.setJob(subscriberAccountDTO.getJob());
        subscriberAccount.setPhone(subscriberAccountDTO.getPhone());

        subscriberAccountRepository.save(subscriberAccount);
        return subscriberAccountMapper.toDto(subscriberAccount);

    }


}

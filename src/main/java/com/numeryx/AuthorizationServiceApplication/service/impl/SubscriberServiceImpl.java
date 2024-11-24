package com.numeryx.AuthorizationServiceApplication.service.impl;

import com.numeryx.AuthorizationServiceApplication.client.RcFeignClient;
import com.numeryx.AuthorizationServiceApplication.dto.ChangeSubscriberDTO;
import com.numeryx.AuthorizationServiceApplication.dto.SubscriberDTO;
import com.numeryx.AuthorizationServiceApplication.dto.request.CreateSubscriberDTO;
import com.numeryx.AuthorizationServiceApplication.exception.EntityNotFoundException;
import com.numeryx.AuthorizationServiceApplication.exception.Reason;
import com.numeryx.AuthorizationServiceApplication.mapper.AddressMapper;
import com.numeryx.AuthorizationServiceApplication.mapper.CreateSubscriberMapper;
import com.numeryx.AuthorizationServiceApplication.mapper.SubscriberMapper;
import com.numeryx.AuthorizationServiceApplication.model.Address;
import com.numeryx.AuthorizationServiceApplication.model.Subscriber;
import com.numeryx.AuthorizationServiceApplication.model.SubscriberAccount;
import com.numeryx.AuthorizationServiceApplication.repository.IAddressRepository;
import com.numeryx.AuthorizationServiceApplication.repository.ISubscriberAccountRepository;
import com.numeryx.AuthorizationServiceApplication.repository.ISubscriberRepository;
import com.numeryx.AuthorizationServiceApplication.service.ISubscriberService;
import com.numeryx.AuthorizationServiceApplication.validator.SubscriberValidator;
import javassist.NotFoundException;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.numeryx.AuthorizationServiceApplication.utilities.Constant.SUBSCRIBER_NOT_FOUND_EXCEPTION;


@Service("subscriberComponent")
public class SubscriberServiceImpl implements ISubscriberService {
    private final Logger log = LoggerFactory.getLogger(SubscriberServiceImpl.class);
    @Autowired
    private ISubscriberRepository subscriberRepository;
    @Autowired
    private IAddressRepository addressRepository;
    @Autowired
    private ISubscriberAccountRepository subscriberAccountRepository;
    @Autowired
    private SubscriberMapper subscriberMapper;
    @Autowired
    private SubscriberValidator subscriberValidator;
    @Autowired
    private CreateSubscriberMapper createSubscriberMapper;
    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private RcFeignClient rcFeignClient;
@Qualifier("subscriberAccountComponent")
 @Autowired
UserAccountManagementImpl userAccountManagement;
    /**
     * Find all subscribers or search subscriber by value
     */
    @Override
    public Page<SubscriberDTO> findAll(String searchValue, Pageable pageable) {
        log.debug("Request to return all subscribers");
        Page<Subscriber> subscribers;
        if(searchValue != null) {
            subscribers = subscriberRepository.searchAllBy(searchValue,pageable == null ? Pageable.unpaged() : (Pageable) pageable.getSort().ascending().and(Sort.by("subscriber_name")));
            log.debug("search value is: " + searchValue + " and results are: " + subscribers);
        }
        else {
            subscribers = subscriberRepository.findAll(pageable == null ? Pageable.unpaged() : (Pageable) pageable.getSort().ascending().and(Sort.by("subscriber_name")));
            log.debug("no search value and results are: " + subscribers);
        }
        return new PageImpl<>(subscriberMapper.toDto(subscribers.getContent()), pageable == null ? Pageable.unpaged() : pageable, subscribers.getTotalElements());
    }

    /**
     * Method to lock a subscriber
     * Locks subscriber accounts as well
     * Activating a subscriber doesn't activate all accounts
     */
    @Override
    public SubscriberDTO lock(Long id, Boolean active) {
        log.debug("Subscriber with id : ", id, " will be " + (active ? "activated" : "deactivated"));
        Subscriber subscriber = subscriberRepository.getOne(id);
        if (subscriber == null) {
            log.debug("subscriber with id ", id, "  not found");
            throw new EntityNotFoundException(Collections.singletonList(new Reason(EntityNotFoundException.ReasonCode.SUBSCRIBER_NOT_FOUND, SUBSCRIBER_NOT_FOUND_EXCEPTION)));
        }
        else {
            subscriber.setActive(active);
            List<SubscriberAccount> subscriberAccounts = subscriberAccountRepository.findByActiveTrueAndSubscriber_Id(subscriber.getId());
            if (!active) {
               subscriber.setHasAccount(active);
                for (SubscriberAccount account : subscriberAccounts) {
                    account.setEnabled(active);
                    subscriberAccountRepository.save(account);
                    log.debug("user with id ", account.getId(), " has been desactivated");
                }
            }
            log.debug("subscriber with id ", id, " has been ", (active ? "activated" : "desactivated"));
            subscriberRepository.save(subscriber);
            return subscriberMapper.toDto(subscriber);
        }
    }
    /**
     * Create a new subscriber
     */
    @Override
    public SubscriberDTO create(CreateSubscriberDTO subscriberDTO) {
        log.debug("attempting to create a new subscriber from CreateSubscriberDTO {}", subscriberDTO);
        subscriberValidator.beforeSave(subscriberDTO);
        log.debug("new subscriber is valid, attempting to save entity");
        Subscriber subscriber = createSubscriberMapper.toEntity(subscriberDTO);
        Address address = addressMapper.toEntity(subscriberDTO.getAddress());
        addressRepository.save(address);
        log.debug("address saved");
        subscriber.setAddress(address);
        subscriberRepository.save(subscriber);
        log.debug("subscriber saved");
        return subscriberMapper.toDto(subscriber);
    }
    /**
     * Update an existing subscriber
     */
    @Override
    public SubscriberDTO update(SubscriberDTO subscriberDTO) {
        log.debug("attempting to update subscriber {}", subscriberDTO);
        subscriberValidator.beforeUpdate(subscriberDTO);
        log.debug("subscriber content is valid, attempting to update entity");
        Subscriber subscriber = subscriberRepository.getOne(subscriberDTO.getId());
        Address address = addressRepository.getOne(subscriberDTO.getAddress().getId());
        address.setCity(subscriberDTO.getAddress().getCity());
        address.setZipCode(subscriberDTO.getAddress().getZipCode());
        address.setComplementaryAddress(subscriberDTO.getAddress().getComplementaryAddress());
        address.setMainAddress(subscriberDTO.getAddress().getMainAddress());
        addressRepository.save(address);
        log.debug("address saved");
        subscriber.setAddress(address);
        subscriber.setCodeNAF(subscriberDTO.getCodeNAF());
        subscriber.setSiret(subscriberDTO.getSiret());
        subscriber.setSubscriberName(subscriberDTO.getSubscriberName());
        subscriber.setCommercialDesignation(subscriberDTO.getCommercialDesignation());
        /* change userMail */
        if(subscriberDTO.getUtilisateurMail() != null) {
            SubscriberAccount subscriberAccount = subscriberAccountRepository.findBySubscriber(Optional.of(subscriber));
            subscriberAccount.setUsername(subscriberDTO.getUtilisateurMail());
            subscriberAccountRepository.save(subscriberAccount);
            userAccountManagement.sendSubscriberSmsAndMailNotification(subscriberAccount);
        }
        ChangeSubscriberDTO changeSubscriberDTO = new ChangeSubscriberDTO();
        changeSubscriberDTO.setId(subscriberDTO.getId());
        changeSubscriberDTO.setName(subscriberDTO.getSubscriberName());
        rcFeignClient.updateSubscriberInContract(changeSubscriberDTO);
        subscriberRepository.save(subscriber);
        log.debug("subscriber saved");
        return subscriberMapper.toDto(subscriber);
    }

    @Override
    public Page<SubscriberDTO> findBySubscriberName(Pageable pageable, String subscriberName) {
        Page<Subscriber> subscribers;
        if (!subscriberName.isEmpty()) {
            subscribers = subscriberRepository.findBySubscriberNameIgnoreCaseContaining(subscriberName, pageable);
        } else {
            subscribers = subscriberRepository.findAll(pageable);
        }
        return
                new PageImpl<>(
                        this.subscriberMapper.toDto(
                                subscribers.getContent()
                        ),
                        pageable,
                        subscribers.getTotalElements()
                );
    }

    @SneakyThrows
    @Override
    public SubscriberDTO findById(Long id) {
        Subscriber subscriber = subscriberRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Subscriber not found"));
        return subscriberMapper.toDto(subscriber);
    }
}

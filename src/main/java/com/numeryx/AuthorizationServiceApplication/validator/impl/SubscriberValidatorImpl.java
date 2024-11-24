package com.numeryx.AuthorizationServiceApplication.validator.impl;

import com.numeryx.AuthorizationServiceApplication.dto.SubscriberDTO;
import com.numeryx.AuthorizationServiceApplication.dto.request.CreateSubscriberDTO;
import com.numeryx.AuthorizationServiceApplication.exception.Reason;
import com.numeryx.AuthorizationServiceApplication.exception.UserException;
import com.numeryx.AuthorizationServiceApplication.exception.ValidationException;
import com.numeryx.AuthorizationServiceApplication.model.Address;
import com.numeryx.AuthorizationServiceApplication.model.Subscriber;
import com.numeryx.AuthorizationServiceApplication.model.SubscriberAccount;
import com.numeryx.AuthorizationServiceApplication.model.User;
import com.numeryx.AuthorizationServiceApplication.repository.IAddressRepository;
import com.numeryx.AuthorizationServiceApplication.repository.ISubscriberAccountRepository;
import com.numeryx.AuthorizationServiceApplication.repository.ISubscriberRepository;
import com.numeryx.AuthorizationServiceApplication.repository.UserRepository;
import com.numeryx.AuthorizationServiceApplication.validator.SubscriberValidator;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import static com.numeryx.AuthorizationServiceApplication.utilities.Constant.*;

@Component("subscriberValidator")
public class SubscriberValidatorImpl implements SubscriberValidator {

    private List<Reason> reasons = new ArrayList<>();
    private final ISubscriberRepository subscriberRepository;
    private final IAddressRepository addressRepository;
    private final UserRepository userRepository;

    public SubscriberValidatorImpl(ISubscriberRepository subscriberRepository, IAddressRepository addressRepository, UserRepository userRepository) {
        this.subscriberRepository = subscriberRepository;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    /**
     * Overridden methods
     */
    @Override
    public void beforeSave(CreateSubscriberDTO createSubscriberDTO) {
        reasons.clear();
        SubscriberDTO subscriberDTO = new SubscriberDTO();
        subscriberDTO.setAddress(createSubscriberDTO.getAddress());
        subscriberDTO.setCodeNAF(createSubscriberDTO.getCodeNAF());
        subscriberDTO.setSiret(createSubscriberDTO.getSiret());
        subscriberDTO.setSubscriberName(createSubscriberDTO.getSubscriberName());
        subscriberDTO.setCommercialDesignation(createSubscriberDTO.getCommercialDesignation());
        /**
         * Check if fields are null
         */
        checkNull(subscriberDTO);
        /**
         * Check if fields are empty
         */
        checkEmpty(subscriberDTO);
        /**
         * Check format validity
         */
        checkValidity(subscriberDTO);
        /**
         * Check if subscriber already exists
         */
        if (exists(subscriberDTO)) {
            reasons.add(new Reason(ValidationException.ReasonCode.ALREADY_EXISTS, SUBSCRIBER_ALREADY_EXISTS));
        }
        if (!reasons.isEmpty())
            throw new ValidationException(reasons);
    }

    @Override
    public void beforeUpdate(SubscriberDTO subscriberDTO) {
        reasons.clear();
        /**
         * Check if occurrence exists are null
         */
        if (subscriberDTO.getId() == null) { // Only before update
            reasons.add(new Reason(ValidationException.ReasonCode.IS_NULL, ID_IS_NULL));
        }
        Subscriber subscriber = subscriberRepository.findById(subscriberDTO.getId()).orElse(null);
        if (!found(subscriber)) {
            reasons.add(new Reason(ValidationException.ReasonCode.DOES_NOT_EXISTS, SUBSCRIBER_DOES_NOT_EXISTS));
        }
        if (subscriber != null && !addressFound(subscriber, subscriberDTO)) {
            reasons.add(new Reason(ValidationException.ReasonCode.DOES_NOT_EXISTS, ADDRESS_DOES_NOT_EXISTS));
        }
        /**
         * Check if fields are null
         */
        checkNull(subscriberDTO);
        if (subscriberDTO.getAddress().getId() == null) { // Only before update
            reasons.add(new Reason(ValidationException.ReasonCode.IS_NULL, ID_IS_NULL));
        }
        /**
         * Check if fields are empty
         */
        checkEmpty(subscriberDTO);
        /**
         * Check format validity
         */
        checkValidity(subscriberDTO);
        if (!reasons.isEmpty())
            throw new ValidationException(reasons);

        /**
         * check if userEmail exists
         */
        if(subscriberDTO.getUtilisateurMail() != null) {
            User user = userRepository.findByUsernameIgnoreCase(subscriberDTO.getUtilisateurMail());
            if (user !=null && (!(user instanceof SubscriberAccount) || ((SubscriberAccount) user).getSubscriber() !=subscriber)) {
                reasons.add(new Reason<>(UserException.ReasonCode.USERNAME_ALREADY_EXIST, USER_ALREADY_EXISTS));
            }
        }
        if (!reasons.isEmpty())
            throw new ValidationException(reasons);
    }


    /**
     * Validation methods
     */

    private void checkNull(SubscriberDTO createSubscriberDTO) {
        /**
         * Check if fields are null
         */
        if (createSubscriberDTO.getSubscriberName() == null) {
            reasons.add(new Reason(ValidationException.ReasonCode.IS_NULL, NULL_SUBSCRIBER_NAME));
        }
        if (createSubscriberDTO.getCodeNAF() == null) {
            reasons.add(new Reason(ValidationException.ReasonCode.IS_NULL, NULL_SUBSCRIBER_NAFCODE));
        }
        if (createSubscriberDTO.getSiret() == null) {
            reasons.add(new Reason(ValidationException.ReasonCode.IS_NULL, NULL_SUBSCRIBER_SIRET));
        }
        if (createSubscriberDTO.getAddress() == null) {
            reasons.add(new Reason(ValidationException.ReasonCode.IS_NULL, NULL_SUBSCRIBER_ADDRESS));
        }
        if (createSubscriberDTO.getAddress() != null && createSubscriberDTO.getAddress().getCity() == null) {
            reasons.add(new Reason(ValidationException.ReasonCode.IS_NULL, NULL_SUBSCRIBER_CITY));
        }
        if (createSubscriberDTO.getAddress() != null && createSubscriberDTO.getAddress().getZipCode() == null) {
            reasons.add(new Reason(ValidationException.ReasonCode.IS_NULL, NULL_SUBSCRIBER_ZIP));
        }
        if (createSubscriberDTO.getAddress() != null && createSubscriberDTO.getAddress().getMainAddress() == null) {
            reasons.add(new Reason(ValidationException.ReasonCode.IS_NULL, NULL_SUBSCRIBER_MAIN_ADDRESS));
        }
      /*  if (createSubscriberDTO.getAddress() != null && createSubscriberDTO.getAddress().getComplementaryAddress() == null) {
            reasons.add(new Reason(ValidationException.ReasonCode.IS_NULL, NULL_SUBSCRIBER_COMPLEMENTARY_ADDRESS));
        }*/
    }

    private void checkEmpty(SubscriberDTO createSubscriberDTO) {
        if (createSubscriberDTO.getSubscriberName() != null && createSubscriberDTO.getSubscriberName().isEmpty()) {
            reasons.add(new Reason(ValidationException.ReasonCode.IS_NULL, EMPTY_SUBSCRIBER_NAME));
        }
        if (createSubscriberDTO.getCodeNAF() != null && createSubscriberDTO.getCodeNAF().isEmpty()) {
            reasons.add(new Reason(ValidationException.ReasonCode.IS_NULL, EMPTY_SUBSCRIBER_NAFCODE));
        }
        if (createSubscriberDTO.getSiret() != null && createSubscriberDTO.getSiret().isEmpty()) {
            reasons.add(new Reason(ValidationException.ReasonCode.IS_NULL, EMPTY_SUBSCRIBER_SIRET));
        }
        if (createSubscriberDTO.getAddress() != null
                && createSubscriberDTO.getAddress().getCity() != null
                && createSubscriberDTO.getAddress().getCity().isEmpty()) {
            reasons.add(new Reason(ValidationException.ReasonCode.IS_NULL, EMPTY_SUBSCRIBER_CITY));
        }
        if (createSubscriberDTO.getAddress() != null
                && createSubscriberDTO.getAddress().getMainAddress() != null
                && createSubscriberDTO.getAddress().getMainAddress().isEmpty()) {
            reasons.add(new Reason(ValidationException.ReasonCode.IS_NULL, EMPTY_SUBSCRIBER_MAIN_ADDRESS));
        }
       /* if (createSubscriberDTO.getAddress() != null
                && createSubscriberDTO.getAddress().getComplementaryAddress() != null
                && createSubscriberDTO.getAddress().getComplementaryAddress().isEmpty()) {
            reasons.add(new Reason(ValidationException.ReasonCode.IS_NULL, EMPTY_SUBSCRIBER_COMPLEMENTARY_ADDRESS));
        }*/
    }

    /**
     * Check if same content exists
     */
    private Boolean exists(SubscriberDTO dto) {
        if ((dto.getSubscriberName() != null && !dto.getSubscriberName().isEmpty())
                && (dto.getSiret() != null && !dto.getSiret().isEmpty())) {
            Subscriber subscriber = subscriberRepository.getFirstBySubscriberNameIgnoreCaseAndSiretIgnoreCaseAndCodeNAFIgnoreCase(
                    dto.getSubscriberName(), dto.getSiret(), dto.getCodeNAF());
            if (subscriber != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if entity with id exists
     */
    private Boolean found(Subscriber subscriber) {
        if (subscriber != null) {
            return true;
        }
        return false;
    }

    private boolean addressFound(Subscriber subscriber, SubscriberDTO subscriberDTO) {
        Address savedAdr = addressRepository.findById(subscriberDTO.getAddress().getId()).orElse(null);
        if (savedAdr != null && subscriber.getAddress().equals(savedAdr)) {
            return true;
        }
        return false;
    }

    private void checkValidity(SubscriberDTO dto) {
        // Nothing for now
    }
}

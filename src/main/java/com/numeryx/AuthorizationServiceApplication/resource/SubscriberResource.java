package com.numeryx.AuthorizationServiceApplication.resource;

import com.numeryx.AuthorizationServiceApplication.dto.SubscriberDTO;
import com.numeryx.AuthorizationServiceApplication.dto.UserDto;
import com.numeryx.AuthorizationServiceApplication.dto.request.CreateSubscriberDTO;
import com.numeryx.AuthorizationServiceApplication.enumeration.RoleEnum;
import com.numeryx.AuthorizationServiceApplication.model.SubscriberAccount;
import com.numeryx.AuthorizationServiceApplication.model.User;
import com.numeryx.AuthorizationServiceApplication.repository.UserRepository;
import com.numeryx.AuthorizationServiceApplication.resource.util.Constants;
import com.numeryx.AuthorizationServiceApplication.service.ISubscriberAccountService;
import com.numeryx.AuthorizationServiceApplication.service.impl.SubscriberAccountServiceImpl;
import com.numeryx.AuthorizationServiceApplication.service.impl.SubscriberServiceImpl;
import com.numeryx.AuthorizationServiceApplication.service.impl.UserServiceImpl;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/api/subscribers")
@RestController
public class SubscriberResource {

    @Autowired
    private SubscriberServiceImpl subscriberServiceImpl;
    @Autowired
    private SubscriberAccountServiceImpl subscriberAccountServiceImpl;


    /**
     * Request to return all subscribers with or without search value
     */
    @PreAuthorize(Constants.HAS_ROLE_SUPER_ADMIN_PROJECT_DIRECTOR)
    @GetMapping("/all")
    @ApiOperation(value = "find all and search by all susbcriber", response = ResponseEntity.class)
    public ResponseEntity<Page<SubscriberDTO>>findAll(@RequestParam(required = false) String searchValue, @ApiParam(required = false)Pageable pageable) {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                        .getRequest();
        String getAll = request.getHeader("get-all");

        List<SubscriberAccount> subscriberAccounts = new ArrayList<SubscriberAccount>();
        subscriberAccounts = subscriberAccountServiceImpl.findAllSubscriber();
        if ("TRUE".equals(getAll)) {
            Page<SubscriberDTO> subscriberDTOS = subscriberServiceImpl.findAll(searchValue, null);
            //subscriberDTOS = subscriberServiceImpl.findAll(searchValue, null);
            for(SubscriberAccount subscriberAccount : subscriberAccounts) {

                if (subscriberAccount.getSubscriber().getId() != null) {
                    for (SubscriberDTO subscriberDTO : subscriberDTOS.getContent()){
                        if((subscriberAccount.getSubscriber().getId() == subscriberDTO.getId()) && subscriberAccount.getRole() == RoleEnum.ROLE_ADMIN_SUBSCRIBER){
                            subscriberDTO.setUtilisateurMail(subscriberAccount.getUsername());
                        }
                    }
                }
            }
            return ResponseEntity.ok(subscriberDTOS);
        } else {
            Page<SubscriberDTO> subscriberDTOS = subscriberServiceImpl.findAll(searchValue, pageable);
            for(SubscriberAccount subscriberAccount : subscriberAccounts) {

                if (subscriberAccount.getSubscriber().getId() != null) {
                    for (SubscriberDTO subscriberDTO : subscriberDTOS.getContent()){
                        if((subscriberAccount.getSubscriber().getId() == subscriberDTO.getId()) && subscriberAccount.getRole() == RoleEnum.ROLE_ADMIN_SUBSCRIBER){
                            subscriberDTO.setUtilisateurMail(subscriberAccount.getUsername());
                        }
                    }
                }
            }
            return ResponseEntity.ok(subscriberDTOS);

        }
    }
    /**
     * Request to lock a subscriber
     */
    @PreAuthorize(Constants.HAS_ROLE_SUPER_ADMIN_PROJECT_DIRECTOR)
    @PutMapping("/{id}/lock")
    @ApiOperation(value = "lock and unlock subscriber", response = ResponseEntity.class)
    public ResponseEntity<SubscriberDTO>lock(@PathVariable(required = true) Long id, @RequestParam(required = true) Boolean active ) {
        return ResponseEntity.ok(subscriberServiceImpl.lock(id, active));
    }
    /**
     * Request to create a new subscriber
     */
    @PreAuthorize(Constants.HAS_ROLE_SUPER_ADMIN_PROJECT_DIRECTOR)
    @PostMapping("/create")
    @ApiOperation(value = "create a new subscriber", response = ResponseEntity.class)
    public ResponseEntity<SubscriberDTO>create(@RequestBody(required = true) CreateSubscriberDTO createSubscriberDTO) {
        SubscriberDTO newSubscriber = subscriberServiceImpl.create(createSubscriberDTO);
        return ResponseEntity.created(URI.create("/api/subscribers/")).body(newSubscriber);
    }
    /**
     * Request to update an existing subscriber
     */
    @PreAuthorize(Constants.HAS_ROLE_SUPER_ADMIN_PROJECT_DIRECTOR)
    @PutMapping("/update")
    @ApiOperation(value = "update an existing subscriber", response = ResponseEntity.class)
    public ResponseEntity<SubscriberDTO>update(@RequestBody(required = true) SubscriberDTO subscriberDTO) {
        return ResponseEntity.ok(subscriberServiceImpl.update(subscriberDTO));
    }
    @PreAuthorize(Constants.HAS_ROLE_SUPER_ADMIN_PROJECT_DIRECTOR)
    @GetMapping("/search")
    @ApiOperation(value = "find all and search by all susbcriber", response = ResponseEntity.class)
    public ResponseEntity<Page<SubscriberDTO>> searchSubscriberByName(@ApiParam Pageable pageable, @RequestParam String subscriberName) {
        return ResponseEntity.ok(subscriberServiceImpl.findBySubscriberName(pageable, subscriberName));
    }

    /**
     * Request to find a subscriber by id
     */
    @PreAuthorize(Constants.HAS_ROLE_SUPER_ADMIN_PROJECT_DIRECTOR)
    @GetMapping("/{id}")
    @ApiOperation(value = "find subscriber by id", response = ResponseEntity.class)
    public ResponseEntity<SubscriberDTO>findById(@PathVariable(required = true) Long id) {
        return ResponseEntity.ok(subscriberServiceImpl.findById(id));
    }
}

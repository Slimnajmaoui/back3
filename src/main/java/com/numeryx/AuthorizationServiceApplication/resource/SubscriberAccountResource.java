package com.numeryx.AuthorizationServiceApplication.resource;

import com.numeryx.AuthorizationServiceApplication.dto.SubscriberAccountDTO;
import com.numeryx.AuthorizationServiceApplication.dto.request.CreateSubscriberAccountDTO;
import com.numeryx.AuthorizationServiceApplication.resource.util.Constants;
import com.numeryx.AuthorizationServiceApplication.service.ISubscriberAccountService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequestMapping("/api/subscriber-account")
@RestController
public class SubscriberAccountResource {

    @Autowired
    private ISubscriberAccountService subscriberAccountService;
    /**
     * Request to add a user for a subscriber
     */
    @PreAuthorize(Constants.HAS_ROLE_SUPER_ADMIN_PROJECT_DIRECTOR)
    @PutMapping("add-user-by-email")
    @ApiOperation(value = "add user account for subscriber by email", response = ResponseEntity.class)
    public ResponseEntity<SubscriberAccountDTO>addUserByEmail(@RequestBody(required = true) CreateSubscriberAccountDTO createSubscriberAccountDTO) {
        return ResponseEntity.ok(subscriberAccountService.addUserByEmail(createSubscriberAccountDTO));
    }

    /**
     * Request to add users for a subscriber
     */
    @PreAuthorize(Constants.HAS_ROLE_ADMIN_SUBSCRIBER)
    @PostMapping("add-account")
    @ApiOperation(value = "add user account for subscriber ", response = ResponseEntity.class)
    public ResponseEntity<SubscriberAccountDTO>addSlaveAccounts(@RequestBody(required = true) SubscriberAccountDTO subscriberAccountDTO) {
        return ResponseEntity.ok(subscriberAccountService.addSlaveAccounts(subscriberAccountDTO));
    }

    @PreAuthorize(Constants.HAS_ROLE_ADMIN_SUBSCRIBER)
    @GetMapping
    @ApiOperation(value = "Find all user subscriber accounts", response = ResponseEntity.class)
    public ResponseEntity<List<SubscriberAccountDTO>> findAll(@RequestParam(required = false) String searchValue) {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                        .getRequest();
        String getAll = request.getHeader("get-all");
        if ("TRUE".equals(getAll)) {
            return ResponseEntity.ok(subscriberAccountService.findUserAccountsByAdmin(searchValue));
        } else {
            return ResponseEntity.ok(subscriberAccountService.findUserAccountsByAdmin(searchValue));
        }
    }

    /**
     * Request to delete user Account
     */
    @PreAuthorize(Constants.HAS_ROLE_ADMIN_SUBSCRIBER)
    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable Long id) {
        subscriberAccountService.deleteSubscriberAccountUser(id);
    }

    /**
     * Request to update users for a subscriber
     */
    @PreAuthorize(Constants.HAS_ROLE_ADMIN_SUBSCRIBER)
    @PutMapping("update")
    @ApiOperation(value = "update slave user account ", response = ResponseEntity.class)
    public ResponseEntity<SubscriberAccountDTO>updateSlaveAccounts(@RequestBody(required = true) SubscriberAccountDTO subscriberAccountDTO) {
        return ResponseEntity.ok(subscriberAccountService.update(subscriberAccountDTO));
    }

}

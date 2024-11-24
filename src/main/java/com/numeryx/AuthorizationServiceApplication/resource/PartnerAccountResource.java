package com.numeryx.AuthorizationServiceApplication.resource;


import com.numeryx.AuthorizationServiceApplication.dto.PartnerAccountDTO;
import com.numeryx.AuthorizationServiceApplication.dto.SubscriberAccountDTO;
import com.numeryx.AuthorizationServiceApplication.dto.request.CreateUserRequest;
import com.numeryx.AuthorizationServiceApplication.resource.util.Constants;
import com.numeryx.AuthorizationServiceApplication.service.IPartnerAccountService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping("api/partner-account")
@RestController
public class PartnerAccountResource {

    @Autowired
    private IPartnerAccountService iPartnerAccountService;

    /**
     * Request to add users for a subscriber
     */
    @PreAuthorize(Constants.HAS_ROLE_SUPER_ADMIN_PARTNER)
    @PostMapping()
    @ApiOperation(value = "add user account for partner ", response = ResponseEntity.class)
    public ResponseEntity<PartnerAccountDTO>addSlaveAccounts(@RequestBody(required = true) CreateUserRequest createUserRequest) {
        return ResponseEntity.ok(iPartnerAccountService.addPartnerAccount(createUserRequest));
    }

    @PreAuthorize(Constants.HAS_ROLE_SUPER_ADMIN_PARTNER)
    @GetMapping()
    public ResponseEntity<Page<PartnerAccountDTO>> getPartnersAccounts(@RequestParam(required = false) String searchKey,
                                                                       @RequestParam Long idAdmin,
                                                                       Pageable pageable) {
        return ResponseEntity.ok().body(iPartnerAccountService.searchPartnerAccounts(searchKey, idAdmin, pageable));
    }

    @DeleteMapping()
    public void delete (@RequestParam Long idPartnerAccount) throws Exception {
        iPartnerAccountService.deletePartnerAccount(idPartnerAccount);
    }

    @PreAuthorize(Constants.HAS_ROLE_SUPER_ADMIN_PARTNER)
    @PutMapping()
    public ResponseEntity<PartnerAccountDTO> updatePartnerAccount(@RequestBody PartnerAccountDTO partnerAccountDTO) {

        return ResponseEntity.ok().body(iPartnerAccountService.updatePartnerAccount(partnerAccountDTO));
    }

    @PreAuthorize(Constants.HAS_ROLE_SUPER_ADMIN_PARTNER)
    @GetMapping("/lock")
    public ResponseEntity<PartnerAccountDTO> lockPartnerAccount(@RequestParam Long idAccount) {
        return ResponseEntity.ok().body(iPartnerAccountService.lockPartnerAccount(idAccount));
    }

}

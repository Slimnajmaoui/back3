package com.numeryx.AuthorizationServiceApplication.resource;


import com.numeryx.AuthorizationServiceApplication.dto.AdminDTO;
import com.numeryx.AuthorizationServiceApplication.dto.UserDto;
import com.numeryx.AuthorizationServiceApplication.dto.request.CreateAdminDTO;
import com.numeryx.AuthorizationServiceApplication.resource.util.Constants;
import com.numeryx.AuthorizationServiceApplication.service.IAdminService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Objects;

@RequestMapping("/api/admin")
@RestController
public class AdminResource {

    private final IAdminService adminService;

    public AdminResource(IAdminService adminService) {
        this.adminService = adminService;
    }

    @PreAuthorize(Constants.HAS_ROLE_SUPER_ADMIN)
    @PostMapping
    public ResponseEntity<AdminDTO> createUser(@RequestBody CreateAdminDTO admin){
        AdminDTO createdAdmin = adminService.create(admin);
        return ResponseEntity.created(URI.create("/api/admin/")).body(createdAdmin);
    }


    @PreAuthorize(Constants.HAS_ROLE_SUPER_ADMIN)
    @GetMapping
    @ApiOperation(value = "Find all admins", response = ResponseEntity.class)
    public ResponseEntity<Page<AdminDTO>> findAll(@RequestParam(required = false) String searchValue,
                                                  @ApiParam(required = false) Pageable pageable) {
        HttpServletRequest request =
                ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                        .getRequest();
        String getAll = request.getHeader("get-all");
        if ("TRUE".equals(getAll)) {
            return ResponseEntity.ok(adminService.findAll(searchValue, null));
        } else {
            return ResponseEntity.ok(adminService.findAll(searchValue, pageable));
        }
    }

    @PreAuthorize(Constants.HAS_ROLE_SUPER_ADMIN)
    @PutMapping("/enable/{id}")
    @ApiOperation(value = "Enable disable user ", response = ResponseEntity.class)
    public ResponseEntity<UserDto> enableDisableUser(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.enableDisableUser(id));
    }

    @PreAuthorize(Constants.HAS_ROLE_SUPER_ADMIN_ADMIN_SUBSCRIBER)
    @PutMapping("/set-block/{id}")
    @ApiOperation(value = "Enable disable user ", response = ResponseEntity.class)
    public ResponseEntity<UserDto> blockUnblockUser(@PathVariable Long id){
        UserDto updatedUser = adminService.setAccountBlocked(id);
        return ResponseEntity.ok(updatedUser);
    }


}

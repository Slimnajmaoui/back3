package com.numeryx.AuthorizationServiceApplication.service.impl;

import com.numeryx.AuthorizationServiceApplication.client.NotificationFeignClient;
import com.numeryx.AuthorizationServiceApplication.dto.AdminDTO;
import com.numeryx.AuthorizationServiceApplication.dto.UserDto;
import com.numeryx.AuthorizationServiceApplication.dto.request.CreateAdminDTO;
import com.numeryx.AuthorizationServiceApplication.enumeration.FunctionEnum;
import com.numeryx.AuthorizationServiceApplication.enumeration.RoleEnum;
import com.numeryx.AuthorizationServiceApplication.exception.Reason;
import com.numeryx.AuthorizationServiceApplication.exception.UsernameNotFoundException;
import com.numeryx.AuthorizationServiceApplication.mapper.AdminMapper;
import com.numeryx.AuthorizationServiceApplication.mapper.AdminResponseMapper;
import com.numeryx.AuthorizationServiceApplication.mapper.UserResponseMapper;
import com.numeryx.AuthorizationServiceApplication.model.Admin;
import com.numeryx.AuthorizationServiceApplication.model.User;
import com.numeryx.AuthorizationServiceApplication.repository.IAdminRepository;
import com.numeryx.AuthorizationServiceApplication.repository.TokenRepository;
import com.numeryx.AuthorizationServiceApplication.repository.UserRepository;
import com.numeryx.AuthorizationServiceApplication.service.IAdminService;
import com.numeryx.AuthorizationServiceApplication.validator.AdminValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static com.numeryx.AuthorizationServiceApplication.utilities.Constant.USER_NOT_FOUND_EXCEPTION;

@Service
public class AdminServiceImpl extends UserAccountManagementImpl  implements IAdminService {

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserResponseMapper userResponseMapper;
    private final AdminValidator adminValidator;
    private final IAdminRepository adminRepository;
    private final AdminResponseMapper adminResponseMapper;
    private final AdminMapper adminMapper;

    public AdminServiceImpl(UserRepository userRepository,
                            LoginAttemptService loginAttemptService,
                            UserResponseMapper userResponseMapper,
                            AdminValidator adminValidator,
                            IAdminRepository adminRepository,
                            AdminResponseMapper adminResponseMapper,
                            AdminMapper adminMapper,
                            TokenRepository tokenRepository,
                            NotificationFeignClient notificationFeignClient) {
        super(userRepository, loginAttemptService, tokenRepository, notificationFeignClient);
        this.userResponseMapper = userResponseMapper;
        this.adminValidator = adminValidator;
        this.adminRepository = adminRepository;
        this.adminResponseMapper = adminResponseMapper;
        this.adminMapper = adminMapper;
    }

    @Override
    @Transactional
    public AdminDTO create(CreateAdminDTO createAdminDTO) {
        logger.debug("Request to save admin {}", createAdminDTO);
        adminValidator.beforeSave(createAdminDTO);
        Admin admin = adminMapper.toEntity(createAdminDTO);
        admin.setEnabled(false);
        admin.setAttempts(0);
        admin.setUsername(admin.getUsername().toLowerCase());
        admin.setRole(FunctionEnum.FINANCIER.equals(admin.getFunction()) ? RoleEnum.ROLE_ADMIN_FINANCIER
                : FunctionEnum.PROJECT_DIRECTOR.equals(admin.getFunction()) ? RoleEnum.ROLE_ADMIN_PROJECT_DIRECTOR
                : FunctionEnum.PARTNER.equals(admin.getFunction()) ? RoleEnum.ROLE_ADMIN_PROVIDER
                : RoleEnum.ROLE_ADMIN_SUBSCRIBER);
        admin = adminRepository.save(admin);

        sendUserSmsAndMailNotification(admin,admin.getFunction().name());
        return adminResponseMapper.toDto(admin);
    }

    @Override
    public AdminDTO update(CreateAdminDTO createAdminDTO) {
        return null;
    }

    @Override
    public AdminDTO findById(Long id) {
        return null;
    }

    @Override
    public Page<AdminDTO> findAll(String searchValue, Pageable pageable) {
        logger.debug("request to find all pageable admins");

        if (searchValue != null && !searchValue.isEmpty()) {
            if (pageable != null) {
                Page<Admin> admins = adminRepository.searchAdmin(searchValue, (Pageable) pageable.getSort().ascending().and(Sort.by("firstname")));
                return new PageImpl<>(adminResponseMapper.toDto(admins.getContent()), pageable, admins.getTotalElements());
            } else {
                List<Admin> adminList = adminRepository.searchAdmin(searchValue);
                return new PageImpl<>(adminResponseMapper.toDto(adminList), Pageable.unpaged(), adminList.size());
            }

        }
        if (pageable != null) {
            Page<Admin> pageOfUsers = adminRepository.findAll(pageable);
            return new PageImpl<>(adminResponseMapper.toDto(pageOfUsers.getContent()), (Pageable) pageable.getSort().ascending().and(Sort.by("firstname")), pageOfUsers.getTotalElements());
        } else  {
            List<Admin> usersList = adminRepository.findAll(Sort.by("firstname").ascending());
            return new PageImpl<>(adminResponseMapper.toDto(usersList), Pageable.unpaged(), usersList.size());
        }
    }

    @Override
    public List<AdminDTO> findAll() {
        logger.debug("request to find all admins");
        return adminResponseMapper.toDto(adminRepository.findAll());
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public UserDto enableDisableUser(Long id) {
       logger.debug("Request to enable disable user with id '{}'", id);
        User existingUser = userRepository
                .findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found", Collections.singletonList((new Reason(UsernameNotFoundException.ReasonCode.USER_NOT_FOUND, USER_NOT_FOUND_EXCEPTION)))));
        existingUser.setEnabled(!existingUser.isEnabled());
        return userResponseMapper.toDto(userRepository.save(existingUser));
    }

    @Override
    @Transactional
    public UserDto setAccountBlocked(Long id) {
        logger.debug("Request to block unblock account {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found",
                        Collections.singletonList((new Reason(UsernameNotFoundException.ReasonCode.USER_NOT_FOUND, USER_NOT_FOUND_EXCEPTION)))));
        user.setNonLocked(!user.isAccountNonLocked());
        user.setAttempts(0);
        return userResponseMapper.toDto(userRepository.save(user));
    }
}

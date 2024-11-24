package com.numeryx.AuthorizationServiceApplication.config;

import com.numeryx.AuthorizationServiceApplication.enumeration.RoleEnum;
import com.numeryx.AuthorizationServiceApplication.model.User;
import com.numeryx.AuthorizationServiceApplication.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class InitializeServiceData implements ApplicationRunner {

/*
    private static final Logger LOGGER = Logger.getLogger(InitializeServiceData.class);
*/

    @Autowired
    private UserRepository userRepository;

    @Value("${credentials.super-admin-user-password}")
    private String adminUserPassword;

    @Value("${credentials.super-admin-user-login}")
    private String adminUserLogin;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        createSuperAdmin();
        resetBlockedUsers();
    }

    private void createSuperAdmin() {
        List<User> superAdminUserList = userRepository.findByRole(RoleEnum.ROLE_SUPER_ADMIN);
        log.debug("Result fetching super-admin in database: " + superAdminUserList.size());
        if (superAdminUserList.isEmpty()) {
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            User superAdminUser = new User();
            superAdminUser.setPassword(bCryptPasswordEncoder.encode(adminUserPassword));
            superAdminUser.setRole(RoleEnum.ROLE_SUPER_ADMIN);
            superAdminUser.setUsername(adminUserLogin);
            superAdminUser.setEnabled(true);
            userRepository.save(superAdminUser);
        }
    }

    private void resetBlockedUsers() {
        List<User> blockedUsers = userRepository.findByNonLocked(false);
        blockedUsers.forEach(el -> {
            el.setNonLocked(true);
            el.setAttempts(0);
        });
        userRepository.saveAll(blockedUsers);
    }
}

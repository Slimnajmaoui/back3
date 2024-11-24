package com.numeryx.AuthorizationServiceApplication.service.impl;

import com.numeryx.AuthorizationServiceApplication.dto.UserDetailsDTO;
import com.numeryx.AuthorizationServiceApplication.exception.Reason;
import com.numeryx.AuthorizationServiceApplication.model.User;
import com.numeryx.AuthorizationServiceApplication.repository.UserRepository;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

import static com.numeryx.AuthorizationServiceApplication.utilities.Constant.USER_NOT_FOUND_EXCEPTION;

@Service("userService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final LoginAttemptService loginAttemptService;

    public UserDetailsServiceImpl(UserRepository userRepository, LoginAttemptService loginAttemptService) {
        this.userRepository = userRepository;
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetailsDTO user = userRepository.findDetailsByUsernameIgnoreCase(username);
        if (user == null) {
            throw new com.numeryx.AuthorizationServiceApplication.exception.UsernameNotFoundException("User not found", Arrays.asList(new Reason(com.numeryx.AuthorizationServiceApplication.exception.UsernameNotFoundException.ReasonCode.USER_NOT_FOUND, USER_NOT_FOUND_EXCEPTION)));
        }
       /* if (!loginAttemptService.isEnabled(user)) {
            throw new LockedException("USER_ACCOUNT_BLOCKED");
        }*/
        return user;
    }
}

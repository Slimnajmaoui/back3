package com.numeryx.AuthorizationServiceApplication.service.impl;

import com.numeryx.AuthorizationServiceApplication.dto.UserDetailsDTO;
import com.numeryx.AuthorizationServiceApplication.exception.Reason;
import com.numeryx.AuthorizationServiceApplication.exception.UsernameNotFoundException;
import com.numeryx.AuthorizationServiceApplication.model.User;
import com.numeryx.AuthorizationServiceApplication.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.numeryx.AuthorizationServiceApplication.utilities.Constant.USER_NOT_FOUND_EXCEPTION;

@Service
public class LoginAttemptService {

    @Value("${authentication.max-attempts}")
    private int MAX_ATTEMPT;
    @Value("${authentication.delay-to-enable-in-minutes}")
    private int DELAY_TO_ENABLE;
    private final UserRepository userRepository;

    private final Logger logger = LoggerFactory.getLogger(LoginAttemptService.class);

    public LoginAttemptService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void loginFailed(String identifiant) {
        logger.debug("Failed to authenticated user with login: {}", identifiant);
        User user = userRepository.findByUsernameIgnoreCase(identifiant);
        if (user == null) {
            throw new UsernameNotFoundException("User not found", Collections.singletonList((new Reason(UsernameNotFoundException.ReasonCode.USER_NOT_FOUND, USER_NOT_FOUND_EXCEPTION))));
        }
        user.setAttempts(user.getAttempts() + 1);
        userRepository.save(user);
    }

    public boolean isEnabled(User user) {
        boolean enabled = user.getAttempts() <= MAX_ATTEMPT;
        if (!enabled) {
            user.setNonLocked(false);
            userRepository.save(user);
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.schedule(() -> enableAccount(user.getId()), DELAY_TO_ENABLE, TimeUnit.MINUTES);
        }
        return enabled;
    }

    private void enableAccount(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found",
                        Arrays.asList(new Reason(UsernameNotFoundException.ReasonCode.USER_NOT_FOUND, USER_NOT_FOUND_EXCEPTION))
                ));
        user.setNonLocked(true);
        user.setAttempts(0);
        userRepository.save(user);
    }
}

package com.numeryx.AuthorizationServiceApplication.resource;

import com.numeryx.AuthorizationServiceApplication.dto.MinifiedUserDTO;
import com.numeryx.AuthorizationServiceApplication.dto.UserDto;
import com.numeryx.AuthorizationServiceApplication.dto.request.ChangeUserConfidentiality;
import com.numeryx.AuthorizationServiceApplication.dto.request.UpdateUserProfileRequest;
import com.numeryx.AuthorizationServiceApplication.exception.Reason;
import com.numeryx.AuthorizationServiceApplication.exception.UserException;
import com.numeryx.AuthorizationServiceApplication.model.User;
import com.numeryx.AuthorizationServiceApplication.repository.UserRepository;
import com.numeryx.AuthorizationServiceApplication.resource.util.Constants;
import com.numeryx.AuthorizationServiceApplication.service.IUserService;
import com.numeryx.AuthorizationServiceApplication.utilities.Constant;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

import static com.numeryx.AuthorizationServiceApplication.exception.UserException.ReasonCode.USER_NOT_FOUND;

@RequestMapping("/api/users")
@RestController
public class UserResource {

    private final IUserService userService;
    private final UserRepository userRepository;

    public UserResource(IUserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/projectDirectorUserNames")
    public ResponseEntity<List<MinifiedUserDTO>> getAllProjectDirectorUserName(){
        return ResponseEntity.ok(userService.getAllProjectDirector());
    }
    @GetMapping("/financierUserNames")
    public ResponseEntity<List<MinifiedUserDTO>> getAllFinancierUserName(){
        return ResponseEntity.ok(userService.getAllFinancier());
    }
    @GetMapping("/me")
    public ResponseEntity<UserDto> getMe(){
        return ResponseEntity.ok(userService.getMe());
    }

    @PostMapping("/userName")
    public ResponseEntity<List<MinifiedUserDTO>> getUserByUserName(@RequestBody List<String> usernames){
        return ResponseEntity.ok(userService.getUserByUserName(usernames));
    }

    @PostMapping("/ids")
    public ResponseEntity<List<MinifiedUserDTO>> getUserByIds(@RequestBody List<Long> ids){
        return ResponseEntity.ok(userService.getUserByIds(ids));
    }

    @PreAuthorize(Constants.HAS_ANY_AUTHORITY)
    @PutMapping("/profile/update")
    @ApiOperation(value = "Update logged in user profile", response = ResponseEntity.class)
    public ResponseEntity<UserDto> updateProfile(@RequestBody UpdateUserProfileRequest updateUserProfileRequest){
        UserDto newProfile = userService.updateProfile(updateUserProfileRequest);
        return ResponseEntity.ok(newProfile);
    }

    @PostMapping("/change-password")
    @ApiOperation(value = "Change user password", response = ResponseEntity.class)
    public ResponseEntity<UserDto> changeUserPassword(@RequestBody ChangeUserConfidentiality changeUserConfidentiality){
        UserDto updatedUser = userService.changeUserPassword(changeUserConfidentiality);
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/subscriberId")
    public ResponseEntity<Long> getSubscriberId(){
        User userChecked = userRepository.findByUsernameIgnoreCase(
                SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        if (userChecked == null) {
            throw new UserException(Collections.singletonList(new Reason(USER_NOT_FOUND, Constant.USER_NOT_FOUND_EXCEPTION)));
        }
        return ResponseEntity.ok(userService.getSubscriberId(userChecked.getId()));
    }

}

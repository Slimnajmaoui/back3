package com.numeryx.AuthorizationServiceApplication.resource;


import com.numeryx.AuthorizationServiceApplication.dto.UserDto;
import com.numeryx.AuthorizationServiceApplication.dto.request.ChangeUserConfidentiality;
import com.numeryx.AuthorizationServiceApplication.dto.request.ChangeUserCredentials;
import com.numeryx.AuthorizationServiceApplication.service.IOpenApiService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/open-api")
@RestController
public class OpenApiResource {

    private final IOpenApiService openApiService;

    public OpenApiResource(IOpenApiService openApiService) {
        this.openApiService = openApiService;
    }

    @PutMapping("/update-credentials")
    @ApiOperation(value = "update user credentials", response = ResponseEntity.class)
    public ResponseEntity<HttpStatus> updateUserCredentials(@RequestBody ChangeUserCredentials changeUserCredentials) {
        openApiService.updateUserCredentials(changeUserCredentials);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/reset-password")
    @ApiOperation(value = "reset password api", response = ResponseEntity.class)
    public ResponseEntity<HttpStatus> resetPassword(@RequestBody String identifiant) {
        openApiService.resetPassword(identifiant);
        return ResponseEntity.accepted().build();
    }


    @GetMapping("/check/activation-code/{id}/{token}/{activationCode}")
    @ApiOperation(value = "check activation code", response = ResponseEntity.class)
    public ResponseEntity<Boolean> checkActivationCode(@PathVariable Long id,
                                                       @PathVariable String token,
                                                       @PathVariable String activationCode,
                                                       @RequestParam(required = false, defaultValue = "false") boolean isResetRequest) {
        return ResponseEntity.ok(openApiService.checkActivationCode(id, token, activationCode, isResetRequest));
    }

    @GetMapping("/check/token/{id}/{token}")
    @ApiOperation(value = "check token", response = ResponseEntity.class)
    public ResponseEntity<Boolean> checkToken(@PathVariable Long id,
                                              @PathVariable String token,
                                              @RequestParam(required = false, defaultValue = "false") boolean hasCode,
                                              @RequestParam(required = false, defaultValue = "false") boolean isResetRequest) {
        return ResponseEntity.ok(openApiService.checkToken(id, token, hasCode, isResetRequest));
    }

    @PostMapping("/email/validate")
    @ApiOperation(value = "Validate user email", response = ResponseEntity.class)
    public ResponseEntity<UserDto> validateUserEmail(@RequestBody ChangeUserConfidentiality changeUserConfidentiality){
        UserDto updatedUser = openApiService.validateUserEmail(changeUserConfidentiality);
        return ResponseEntity.ok(updatedUser);
    }

}

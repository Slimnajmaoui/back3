package com.numeryx.AuthorizationServiceApplication.client;

import com.numeryx.AuthorizationServiceApplication.dto.request.ResetTokenRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "notificationFeignClient", url = "${feign.client.config.apis.notification}")
public interface NotificationFeignClient {
    @RequestMapping(method = RequestMethod.POST, value = "/email/reset", produces = "application/json")
    ResponseEntity sendResetPassword(@RequestBody ResetTokenRequest req);

    @RequestMapping(method = RequestMethod.POST, value = "/email/resetForSubscriber", produces = "application/json")
    ResponseEntity sendResetPasswordForSubscriber(@RequestBody ResetTokenRequest req);

    @RequestMapping(method = RequestMethod.POST, value = "/email/request", produces = "application/json")
    ResponseEntity sendRequestPassword(@RequestBody ResetTokenRequest req, @RequestParam(value = "function",required = false) String function);

    @RequestMapping(method = RequestMethod.POST, value = "/email/requestSubscriber", produces = "application/json")
    ResponseEntity sendRequestPasswordSubscriber(@RequestBody ResetTokenRequest req);

    @RequestMapping(method = RequestMethod.POST, value = "/sms/activation_code", produces = "application/json")
    ResponseEntity sendActivationCode(@RequestBody ResetTokenRequest req);

    @RequestMapping(method = RequestMethod.POST, value = "/email/info/change-password", produces = "application/json")
    ResponseEntity sendChangePasswordInfoEmail(@RequestBody ResetTokenRequest req);

    @RequestMapping(method = RequestMethod.POST, value = "/email/info/change-phone", produces = "application/json")
    ResponseEntity sendChangePhoneInfoEmail(@RequestBody ResetTokenRequest req);

    @RequestMapping(method = RequestMethod.POST, value = "/email/request/change-email", produces = "application/json")
    ResponseEntity sendTokenChangeEmailRequestEmail(@RequestBody ResetTokenRequest req);

    @RequestMapping(method = RequestMethod.POST, value = "/email/info/change-email-request", produces = "application/json")
    ResponseEntity sendChangeEmailRequestInfoEmail(@RequestBody ResetTokenRequest req);

    @RequestMapping(method = RequestMethod.POST, value = "/email/info/change-email", produces = "application/json")
    ResponseEntity sendChangeEmailInfoEmail(@RequestBody ResetTokenRequest req);

}

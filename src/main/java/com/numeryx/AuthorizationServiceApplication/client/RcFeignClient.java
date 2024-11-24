package com.numeryx.AuthorizationServiceApplication.client;

import com.numeryx.AuthorizationServiceApplication.dto.ChangeSubscriberDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "rcFeignClient", url = "${feign.client.config.apis.rc}")
public interface RcFeignClient {
    @RequestMapping(method = RequestMethod.POST, value = "/api/subscriber/contract", produces = "application/json")
    ResponseEntity updateSubscriberInContract(@RequestBody(required = true) ChangeSubscriberDTO changeSubscriberDTO);
}

package com.numeryx.AuthorizationServiceApplication.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateSubscriberAccountDTO {

    private String email;
    private Long idSubscriber;
}

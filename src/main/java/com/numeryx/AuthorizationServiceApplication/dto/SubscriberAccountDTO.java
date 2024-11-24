package com.numeryx.AuthorizationServiceApplication.dto;

import com.numeryx.AuthorizationServiceApplication.model.SubscriberAccount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriberAccountDTO extends UserDto {

    private List<SubscriberAccountDTO> delegates;
    private SubscriberDTO subscriber;
}

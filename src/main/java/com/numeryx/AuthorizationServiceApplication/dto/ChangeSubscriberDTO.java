package com.numeryx.AuthorizationServiceApplication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeSubscriberDTO {
    private Long id;
    private String name;
}

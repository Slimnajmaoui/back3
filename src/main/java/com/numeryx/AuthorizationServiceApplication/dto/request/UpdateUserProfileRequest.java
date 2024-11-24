package com.numeryx.AuthorizationServiceApplication.dto.request;

import com.numeryx.AuthorizationServiceApplication.dto.BaseUserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UpdateUserProfileRequest implements Serializable {

    private Long id;
    private String username;
    private String firstname;
    private String lastname;
    private String job;
    private String phone;

}

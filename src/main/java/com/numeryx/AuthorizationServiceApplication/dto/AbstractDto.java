package com.numeryx.AuthorizationServiceApplication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractDto implements Serializable {
    private Long id;
    private Date createdDate;
    private Date updatedDate;
    private boolean active;
    private String createdBy;

}

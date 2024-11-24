package com.numeryx.AuthorizationServiceApplication.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjetDto extends AbstractDto{

    private String label;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String description;
    private Double budget;
}

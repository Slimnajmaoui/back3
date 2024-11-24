package com.numeryx.AuthorizationServiceApplication.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Projet extends AbstractEntity{
    private String label;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String description;
    private Double budget;
}

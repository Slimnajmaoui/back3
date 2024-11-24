package com.numeryx.AuthorizationServiceApplication.dto;

import java.time.LocalDate;

public class TacheDto extends AbstractDto{

    private String label;
    private String description;
    private String statutActuel;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String priority;
    private Long projetId;
    private Long userId;
}

package com.numeryx.AuthorizationServiceApplication.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public enum TacheStatutEnum {
    TODO("A faire"),
    EN_COURS("En cours"),
    TERMINER("Terminé");

    private String label;
}

package com.numeryx.AuthorizationServiceApplication.model;

import com.numeryx.AuthorizationServiceApplication.enumeration.TacheStatutEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Tache extends AbstractEntity{

    private String label;
    private String description;
    @Enumerated(EnumType.STRING)
    private TacheStatutEnum statutActuel;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String priority;
    @ManyToOne(cascade =  {CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "projet_id")
    private Projet projet;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}

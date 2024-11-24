package com.numeryx.AuthorizationServiceApplication.service;

import com.numeryx.AuthorizationServiceApplication.dto.ProjetDto;
import com.numeryx.AuthorizationServiceApplication.model.Projet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IProjetService {

    Projet createProjet(ProjetDto projetDto);
    Page<Projet> findAllProjet(Pageable pageable);

    Projet update(ProjetDto projetDto);

    void deleteProjet(Long projetId);
}

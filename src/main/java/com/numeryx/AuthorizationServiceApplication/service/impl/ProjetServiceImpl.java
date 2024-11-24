package com.numeryx.AuthorizationServiceApplication.service.impl;

import com.numeryx.AuthorizationServiceApplication.dto.ProjetDto;
import com.numeryx.AuthorizationServiceApplication.exception.EntityNotFoundException;
import com.numeryx.AuthorizationServiceApplication.exception.Reason;
import com.numeryx.AuthorizationServiceApplication.mapper.ProjetMapper;
import com.numeryx.AuthorizationServiceApplication.model.Projet;
import com.numeryx.AuthorizationServiceApplication.repository.IProjetRepository;
import com.numeryx.AuthorizationServiceApplication.service.IProjetService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;

import static com.numeryx.AuthorizationServiceApplication.utilities.Constant.INVALID_EMAIL_USER_EXCEPTION;

@Service
@RequiredArgsConstructor
public class ProjetServiceImpl implements IProjetService {

    private final IProjetRepository projetRepository;
    private final ProjetMapper projetMapper;
    @Override
    public Projet createProjet(ProjetDto projetDto) {
        Projet projet = projetMapper.toEntity(projetDto);
        return projetRepository.save(projet);
    }

    @Override
    public Page<Projet> findAllProjet(Pageable pageable) {
        return projetRepository.findAll(pageable);
    }

    @Override
    public Projet update(ProjetDto projetDto) {
        Projet projet = projetMapper.toEntity(projetDto);
        if(projet.getId() == null) {
            throw new EntityNotFoundException(
                    Collections.singletonList(
                            new Reason(EntityNotFoundException.ReasonCode.ENTITY_NOT_FOUND)));
        }
        return projetRepository.save(projet);
    }

    @Override
    public void deleteProjet(Long projetId) {
        projetRepository.deleteById(projetId);
    }
}

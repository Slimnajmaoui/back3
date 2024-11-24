package com.numeryx.AuthorizationServiceApplication.resource;

import com.numeryx.AuthorizationServiceApplication.dto.MinifiedUserDTO;
import com.numeryx.AuthorizationServiceApplication.dto.ProjetDto;
import com.numeryx.AuthorizationServiceApplication.model.Projet;
import com.numeryx.AuthorizationServiceApplication.repository.IProjetRepository;
import com.numeryx.AuthorizationServiceApplication.service.IProjetService;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/projet")
@RestController
@RequiredArgsConstructor
public class ProjetRessource {

    private final IProjetService projetService;

    @GetMapping("/find-all")
    public ResponseEntity<Page<Projet>> getAll(@ApiParam(required = false) Pageable pageable){
        return ResponseEntity.ok(projetService.findAllProjet(pageable));
    }

    @PostMapping("/create")
    public ResponseEntity<Projet> create(@RequestBody ProjetDto projetDto){
        return ResponseEntity.ok(projetService.createProjet(projetDto));
    }


    @PostMapping("/delete")
    public void delete(@RequestBody Long projetId){
        projetService.deleteProjet(projetId);
    }
}

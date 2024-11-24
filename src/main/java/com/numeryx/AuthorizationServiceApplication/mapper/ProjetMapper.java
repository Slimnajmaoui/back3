package com.numeryx.AuthorizationServiceApplication.mapper;

import com.numeryx.AuthorizationServiceApplication.dto.ProjetDto;
import com.numeryx.AuthorizationServiceApplication.dto.TacheDto;
import com.numeryx.AuthorizationServiceApplication.model.Projet;
import com.numeryx.AuthorizationServiceApplication.model.Tache;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ProjetMapper extends EntityMapper<ProjetDto, Projet> {
}

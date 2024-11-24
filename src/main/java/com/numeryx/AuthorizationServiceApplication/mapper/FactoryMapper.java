package com.numeryx.AuthorizationServiceApplication.mapper;

import java.util.List;
import java.util.Set;

public interface FactoryMapper<D, E, C, U, T> extends EntityMapper<D, E> {
    E fromCreateRequest(C createRequest);

    E fromUpdateRequest(U updateRequest);

    E fromThirdParty(T thirdParty);

    T toThirdParty(E entity);

    T toThirdPartyFromDTO(D dto);

    List<E> fromCreateRequest(List<C> createRequestList);

    Set<E> fromCreateRequest(Set<C> createRequestSet);

    List<E> fromUpdateRequest(List<U> createRequestList);

    Set<E> fromUpdateRequest(Set<U> createRequestSet);

    List<E> fromThirdParty(List<T> thirdPartyList);

    Set<E> fromThirdParty(Set<T> thirdPartySet);

    List<T> toThirdParty(List<E> thirdPartyList);

    Set<T> toThirdParty(Set<E> thirdPartySet);

    List<T> toThirdPartyFromDTO(List<D> thirdPartyList);

    Set<T> toThirdPartyFromDTO(Set<D> thirdPartySet);
}

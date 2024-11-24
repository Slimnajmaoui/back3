package com.numeryx.AuthorizationServiceApplication.service;

import com.numeryx.AuthorizationServiceApplication.dto.UserDto;
import com.numeryx.AuthorizationServiceApplication.dto.request.CreateUserRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 *
 * @param <C> for create request DTO
 * @param <D> returned DTO
 */
public interface IUserManagement<C extends CreateUserRequest, D extends UserDto> {

    D create(C c);
    D update(C c);
    D findById(Long id);
    Page<D> findAll(String searchValue, Pageable pageable);
    List<D> findAll();
    void delete(Long id);

}

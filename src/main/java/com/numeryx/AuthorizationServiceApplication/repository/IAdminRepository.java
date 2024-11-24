package com.numeryx.AuthorizationServiceApplication.repository;

import com.numeryx.AuthorizationServiceApplication.model.Admin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IAdminRepository extends JpaRepository<Admin, Long> {

    @Query("select a from Admin a" +
            " where lower(concat( a.firstname, ' ', a.lastname)) like lower(concat('%', ?1, '%'))" +
            " or  lower(concat( a.lastname, ' ', a.firstname)) like lower(concat('%', ?1, '%'))" +
            " or lower(a.firstname) like lower(concat('%', ?1, '%'))" +
            " or lower(a.lastname)  like lower(concat('%', ?1, '%'))" +
            " or  lower(a.username) like lower(concat('%', ?1, '%'))" +
             "or lower(a.phone) like lower(concat('%', ?1, '%') ) ")
    Page<Admin> searchAdmin(String searchValue, Pageable pageable);

    @Query("select a from Admin a" +
            " where lower(concat( a.firstname, ' ', a.lastname)) like lower(concat('%', ?1, '%'))" +
            " or  lower(concat( a.lastname, ' ', a.firstname)) like lower(concat('%', ?1, '%'))" +
            " or lower(a.firstname) like lower(concat('%', ?1, '%'))" +
            " or lower(a.lastname)  like lower(concat('%', ?1, '%'))" +
            " or  lower(a.username) like lower(concat('%', ?1, '%'))" +
            "or lower(a.phone) like lower(concat('%', ?1, '%') ) ")
    List<Admin> searchAdmin(String searchValue);

    Admin findByUsernameIgnoreCase(String username);

}

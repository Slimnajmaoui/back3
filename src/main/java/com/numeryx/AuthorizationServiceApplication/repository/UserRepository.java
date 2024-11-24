package com.numeryx.AuthorizationServiceApplication.repository;

import com.numeryx.AuthorizationServiceApplication.dto.UserDetailsDTO;
import com.numeryx.AuthorizationServiceApplication.enumeration.RoleEnum;
import com.numeryx.AuthorizationServiceApplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;
import javax.persistence.QueryHint;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
    @Query("select new com.numeryx.AuthorizationServiceApplication.dto.UserDetailsDTO(u.id, u.username, u.password, u.role, u.attempts, u.nonLocked, u.enabled) from User as u where upper(u.username) like upper(concat('%', ?1, '%'))")
    UserDetailsDTO findDetailsByUsernameIgnoreCase(String userName);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
    User findByUsernameIgnoreCase(String userName);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
    List<User> findByRole(RoleEnum role);


    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
    @Query("select user from User user where user.id <> ?1 and (user.username = ?2 or user.usernameChange = ?2)")
    User findByIdNotAndUsernameIgnoreCaseOrUsernameChangeIgnoreCase(Long id, String userNameChange);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
    List<User> findByNonLocked(boolean nonLocked);

    boolean existsByUsernameIgnoreCase(String username);

    User findByIdAndActiveTrue(Long id);

    List<User> getUsersByIdIsIn(List<Long> id);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
    @Query(value = "select * from users where role = :role",nativeQuery = true)
    List<User> getAllProjectDirectorUserNames(Integer role);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
    @Query(value = "select * from users where role = :role",nativeQuery = true)
    List<User> getAllFinancierUserNames(Integer role);

//    User findFirstByIdAndRoleEquals(Long id, RoleEnum roleEnum);

    User findFirstById(Long id);



}

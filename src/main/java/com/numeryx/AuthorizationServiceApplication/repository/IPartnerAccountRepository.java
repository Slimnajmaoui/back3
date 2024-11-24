package com.numeryx.AuthorizationServiceApplication.repository;

import com.numeryx.AuthorizationServiceApplication.model.PartnerAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IPartnerAccountRepository extends JpaRepository<PartnerAccount, Long> {
    PartnerAccount findByUsernameIgnoreCase(String username);

    @Query("select a from PartnerAccount a" +
            " where (a.partnerAccount.id=?2) and (lower(concat( a.firstname, ' ', a.lastname)) like lower(concat('%', ?1, '%'))" +
            " or  lower(concat( a.lastname, ' ', a.firstname)) like lower(concat('%', ?1, '%'))" +
            " or lower(a.firstname) like lower(concat('%', ?1, '%'))" +
            " or lower(a.lastname)  like lower(concat('%', ?1, '%'))" +
            " or  lower(a.username) like lower(concat('%', ?1, '%'))" +
            "or lower(a.phone) like lower(concat('%', ?1, '%')) ) ")
    Page<PartnerAccount> searchPartnerAccounts(String searchValue, long idAdmin, Pageable pageable);

    @Query("select a from PartnerAccount a where a.partnerAccount.id = :idAdmin ")
    Page<PartnerAccount> getPartnerAccounts(long idAdmin, Pageable pageable);
}

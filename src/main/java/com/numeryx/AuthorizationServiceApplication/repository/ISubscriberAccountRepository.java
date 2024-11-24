package com.numeryx.AuthorizationServiceApplication.repository;

import com.numeryx.AuthorizationServiceApplication.model.Subscriber;
import com.numeryx.AuthorizationServiceApplication.model.SubscriberAccount;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ISubscriberAccountRepository extends JpaRepository<SubscriberAccount, Long> {
    List<SubscriberAccount> findByActiveTrueAndSubscriber_Id(Long id);
    SubscriberAccount findByUsernameIgnoreCaseAndSubscriber_Id(String username, Long subscriberId);
    SubscriberAccount findBySubscriber(Optional<Subscriber> subscriber);
    SubscriberAccount findByUsernameIgnoreCase(String username);
    List<SubscriberAccount> findSubscriberAccountsBySubscriberAccount(SubscriberAccount subscriberAccount, Sort var1);

    @Query("select a from SubscriberAccount a" +
            " where (a.subscriberAccount.id=?2) and (lower(concat( a.firstname, ' ', a.lastname)) like lower(concat('%', ?1, '%'))" +
            " or  lower(concat( a.lastname, ' ', a.firstname)) like lower(concat('%', ?1, '%'))" +
            " or lower(a.firstname) like lower(concat('%', ?1, '%'))" +
            " or lower(a.lastname)  like lower(concat('%', ?1, '%'))" +
            " or  lower(a.username) like lower(concat('%', ?1, '%'))" +
            "or lower(a.phone) like lower(concat('%', ?1, '%')) ) ")
    List<SubscriberAccount> searchSubscriberAccountUsers(String searchValue, long idAdmin);
}

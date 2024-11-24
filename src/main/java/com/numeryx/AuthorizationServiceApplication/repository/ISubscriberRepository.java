package com.numeryx.AuthorizationServiceApplication.repository;

import com.numeryx.AuthorizationServiceApplication.model.Subscriber;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import javax.persistence.QueryHint;
import java.util.List;

public interface ISubscriberRepository extends JpaRepository<Subscriber, Long> {
    @Query(value = "select * from subscriber s inner join address a on a.id = s.address_id where " +
            "upper(s.subscriber_name)  like upper(concat('%', ?1, '%')) " +
            "or upper(s.commercial_designation)  like upper(concat('%', ?1, '%')) " +
            "or upper(s.codenaf)  like upper(concat('%', ?1, '%')) " +
            "or upper(s.siret)  like upper(concat('%', ?1, '%')) " +
            "or upper(a.main_address)  like upper(concat('%', ?1, '%')) " +
            "or upper(a.complementary_address)  like upper(concat('%', ?1, '%')) " +
            "or upper(a.city)  like upper(concat('%', ?1, '%')) " +
            "or upper(CAST(a.zip_code as varchar(10)))  like upper(concat('%', ?1, '%'))",nativeQuery = true)
    Page<Subscriber> searchAllBy(String searchValue, Pageable pageable);

    Subscriber getFirstBySubscriberNameIgnoreCaseAndSiretIgnoreCaseAndCodeNAFIgnoreCase(String name, String siret, String naf);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
    Page<Subscriber> findBySubscriberNameIgnoreCaseContaining (String subscriberName, Pageable pageable);

    @Query("select s.id from SubscriberAccount subAcc " +
            " join Subscriber s on subAcc.subscriber.id = s.id " +
            " where subAcc.id = :userId")
    List<Long> findSubscribersIdByUserId(@Param("userId") Long userId);
}

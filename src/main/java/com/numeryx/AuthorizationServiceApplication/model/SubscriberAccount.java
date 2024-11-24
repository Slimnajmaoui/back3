package com.numeryx.AuthorizationServiceApplication.model;

import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.proxy.HibernateProxy;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;


@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("subscriber")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class SubscriberAccount extends User{

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "subscriberAccount")
    @ToString.Exclude
    private List<SubscriberAccount> delegates;

    @ManyToOne(cascade =  {CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH})
    private SubscriberAccount subscriberAccount;

    @ManyToOne(cascade =  {CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "subscriber_id")
    private Subscriber subscriber;

    @Override
    public String toString() {
        return "SubscriberAccount{" +
                "username='" + getUsername() + '\'' +
                ", password='" + getPassword() + '\'' +
                ", firstname='" + getFirstname() + '\'' +
                ", lastname='" + getLastname() + '\'' +
                ", job='" + getJob() + '\'' +
                ", phone='" + getPhone() + '\'' +
                ", phoneChange='" + getPhoneChange() + '\'' +
                ", usernameChange='" + getUsernameChange() + '\'' +
                ", enabled=" + isEnabled() + '\'' +
                ", attempts=" + getAttempts() + '\'' +
                ", nonLocked=" + isNonLocked() + '\'' +
                ", role=" + getRole() + '\'' +
                ", subscriber=" + getSubscriber() != null ? getSubscriber().toString() : "" + '\'' +
                ", delegates=" + getDelegates() != null ? getDelegates().toString() : "" +
                '}';
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        SubscriberAccount that = (SubscriberAccount) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}

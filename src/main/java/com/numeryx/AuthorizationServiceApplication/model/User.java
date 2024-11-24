package com.numeryx.AuthorizationServiceApplication.model;

import com.numeryx.AuthorizationServiceApplication.enumeration.RoleEnum;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;


@Entity
@Table(name = "users")
@Getter
@Setter
@RequiredArgsConstructor
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type",
        discriminatorType = DiscriminatorType.STRING,
        columnDefinition = "VARCHAR(255)"
)
public class User extends AbstractEntity implements UserDetails {

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "job")
    private String job;

    @Column(name = "phone")
    private String phone;

    @Column(name = "phone_change")
    private String phoneChange;

    @Column(name = "usrname_change", unique = true)
    private String usernameChange;

    @Column(name = "enabled")
    private boolean enabled = true;

    @Column(name = "attempts")
    private int attempts;

    @Column(name = "non_locked")
    private boolean nonLocked = true;


    private RoleEnum role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList(getRole().toString());
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return nonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }


    public User buildUser(User user) {
        if (user.getUsername() != null) {
            this.setUsername(user.getUsername());
        }
        if (user.getPhone() != null) {
            this.setPhone(user.getPhone());
        }
        if (user.getLastname() != null) {
            this.setLastname(user.getLastname());
        }
        if (user.getFirstname() != null) {
            this.setFirstname(user.getFirstname());
        }
        if (user.getJob() != null) {
            this.setJob(user.getJob());
        }
        return this;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", job='" + job + '\'' +
                ", phone='" + phone + '\'' +
                ", phoneChange='" + phoneChange + '\'' +
                ", usernameChange='" + usernameChange + '\'' +
                ", enabled=" + enabled +
                ", attempts=" + attempts +
                ", nonLocked=" + nonLocked +
                ", role=" + role +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        User user = (User) o;
        return getId() != null && Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}

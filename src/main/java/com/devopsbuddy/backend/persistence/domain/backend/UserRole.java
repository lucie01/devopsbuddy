package com.devopsbuddy.backend.persistence.domain.backend;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user_role")
public class UserRole implements Serializable {

    /**
     * The serial version UID for Serializable classes.
    */
    private static final long serialVersionUID = 1L;
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    public UserRole(User user, Role role){
        this.user = user;
        this.role = role;
    }

    @Override
    public int hashCode() {
        int result = user.hashCode();
        result = 31 * result + role.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;

        UserRole userRole = (UserRole) obj;

        if(!user.equals(userRole.getUser())) return false;
        return role.equals(userRole.role);
    }
}

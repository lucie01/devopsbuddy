package com.devopsbuddy.backend.persistence.domain.backend;

import com.devopsbuddy.enums.RolesEnum;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Slf4j
public class Role implements Serializable {

    /**
     * The serial version UID for Serializable classes.
    */
    private static final long serialVersionUID = 1L;

    @Id
    private int id;
    private String name;

    public Role(RolesEnum rolesEnum){
        log.trace("Creating a new Role by RolesEnum with id={} and name={}", rolesEnum.getId(), rolesEnum.getRoleName());
        this.id = rolesEnum.getId();
        this.name = rolesEnum.getRoleName();
    }

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<UserRole> userRoles = new HashSet<>();

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;

        Role role = (Role) obj;
        return id == role.id;
    }

}

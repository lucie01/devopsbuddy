package com.devopsbuddy.backend.persistence.domain.backend;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@NoArgsConstructor
@Data
@Entity
public class Plan implements Serializable {

    /**
     * The serial version UID for Serializable classes.
    */
    private static final long serialVersionUID = 1L;
    @Id
    private int id;
    private String name;
    /*
    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;

        Plan plan = (Plan) obj;
        return id == plan.id;
    }

     */

}

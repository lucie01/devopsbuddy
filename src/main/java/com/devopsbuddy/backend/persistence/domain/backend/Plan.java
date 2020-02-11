package com.devopsbuddy.backend.persistence.domain.backend;

import com.devopsbuddy.enums.PlansEnum;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@NoArgsConstructor
@Data
@Entity
@Slf4j
public class Plan implements Serializable {

    /**
     * The serial version UID for Serializable classes.
    */
    private static final long serialVersionUID = 1L;
    @Id
    private int id;
    private String name;

    public Plan(PlansEnum plansEnum) {
        log.trace("Creating a Plan by PlansEnum with id={} and name={}", plansEnum.getId(), plansEnum.getPlanName());
        this.id = plansEnum.getId();
        this.name = plansEnum.getPlanName();
    }
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

package com.devopsbuddy.enums;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Getter
@Slf4j
public enum PlansEnum {
    BASIC(1, "Basic"),
    PRO(2, "Pro");

    private final int id;
    private final String planName;

    PlansEnum(int id, String planName){
        this.id = id;
        this.planName = planName;
    }
}

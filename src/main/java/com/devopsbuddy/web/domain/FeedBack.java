package com.devopsbuddy.web.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class FeedBack implements Serializable {

    private static final long serialVersionUID = 1L;

    private String email;
    private String firstName;
    private String lastName;
    private String feedBack;
}

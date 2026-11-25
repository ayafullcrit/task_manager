package com.phong.testsec.dto.request;

import lombok.*;

// request/RegisterRequest.java
@Getter
@Setter
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
}

package com.phong.testsec.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest
{
    @NotBlank(message = "name không được để trống")
    private String name;

    @NotBlank(message = "email không được để trống")
    @Email(message = "Email không đúng định dạng")
    private String email;
}

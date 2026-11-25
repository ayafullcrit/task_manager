package com.phong.testsec.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse
{
    public long id;
    public String name;
    public String email;
    public LocalDateTime createdAt;
}

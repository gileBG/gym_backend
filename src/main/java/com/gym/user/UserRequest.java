package com.gym.user;

import lombok.Data;

@Data
public class UserRequest {
    private String ime;
    private String prezime;
    private String email;
    private String lozinka;
    private Role rola;
}
package com.gym.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private Long id;
    private String email;
    private String ime;
    private String prezime;
    private String rola;
}

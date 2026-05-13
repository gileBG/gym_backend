package com.gym.auth;

import com.gym.user.Role;
import lombok.Data;

@Data
public class RegisterRequest {
    private String ime;
    private String prezime;
    private String email;
    private String lozinka;
    private Role rola;
}

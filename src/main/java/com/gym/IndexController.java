package com.gym;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @GetMapping("/")
    @ResponseBody
    public String index() {
        return "<!DOCTYPE html><html><head><title>Gym API</title></head><body>" +
                "<h1>Gym Management System</h1>" +
                "<p>API je pokrenuta na http://localhost:8080</p>" +
                "<h2>Dostupni endpointi:</h2>" +
                "<ul>" +
                "<li>GET /api/roles - Sve dostupne role</li>" +
                "<li>POST /api/auth/register - Registracija</li>" +
                "<li>POST /api/auth/login - Prijava</li>" +
                "<li>GET /api/vezbaci - Svi vezbaci (sa JWT)</li>" +
                "<li>POST /api/vezbaci - Kreiranje vezbaca (sa JWT)</li>" +
                "<li>GET /api/h2-console - H2 baza</li>" +
                "</ul>" +
                "<hr>" +
                "<h2>Testiraj registraciju:</h2>" +
                "<form method='POST' action='/api/auth/register'>" +
                "<input type='text' name='ime' placeholder='Ime' required><br>" +
                "<input type='text' name='prezime' placeholder='Prezime' required><br>" +
                "<input type='email' name='email' placeholder='Email' required><br>" +
                "<input type='password' name='lozinka' placeholder='Lozinka' required><br>" +
                "<input type='text' name='rola' placeholder='ADMIN/TRENER/FRONT_DESK/CISTACICA/MENADZER' required><br>" +
                "<button type='submit'>Registruj se</button>" +
                "</form>" +
                "</body></html>";
    }
}

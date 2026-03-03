package elisaraeli.travelers_notebook_valley_backend.services;

import elisaraeli.travelers_notebook_valley_backend.entities.Utente;
import elisaraeli.travelers_notebook_valley_backend.exceptions.UnauthorizedException;
import elisaraeli.travelers_notebook_valley_backend.payloads.LoginDTO;
import elisaraeli.travelers_notebook_valley_backend.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UtenteService utenteService;
    private final JWTTools jwtTools;
    private final PasswordEncoder bcrypt;

    @Autowired
    public AuthService(UtenteService utenteService, JWTTools jwtTools, PasswordEncoder bcrypt) {
        this.utenteService = utenteService;
        this.jwtTools = jwtTools;
        this.bcrypt = bcrypt;
    }

    public String checkCredentialAndGenerateToken(LoginDTO body) {

        // cerco l'utente tramite email
        Utente utenteTrovato = this.utenteService.findByEmail(body.email());

        // controllo che la password corrisponda e genero il token
        if (bcrypt.matches(body.password(), utenteTrovato.getPassword())) {
            String accessToken = jwtTools.generateToken(utenteTrovato);
            return accessToken;
        } else {
            throw new UnauthorizedException("Le tue credenziali sono errate!");
        }
    }
}

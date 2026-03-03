package elisaraeli.travelers_notebook_valley_backend.services;

import elisaraeli.travelers_notebook_valley_backend.entities.Utente;
import elisaraeli.travelers_notebook_valley_backend.entities.UtenteRuolo;
import elisaraeli.travelers_notebook_valley_backend.exceptions.BadRequestException;
import elisaraeli.travelers_notebook_valley_backend.exceptions.NotFoundException;
import elisaraeli.travelers_notebook_valley_backend.payloads.UtenteResponse;
import elisaraeli.travelers_notebook_valley_backend.payloads.UtentiDTO;
import elisaraeli.travelers_notebook_valley_backend.repositories.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UtenteService {

    private final UtenteRepository utenteRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UtenteService(UtenteRepository utenteRepository, PasswordEncoder passwordEncoder) {
        this.utenteRepository = utenteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Utente findById(UUID id) {
        return utenteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }

    public Utente findByEmail(String email) {
        return utenteRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("L'utente con questa email non è stato trovato."));
    }

    public Utente findByUsername(String username) {
        return utenteRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("L'utente con questo username non è stato trovato."));
    }

    public UtenteResponse register(UtentiDTO payload) {

        // Controllo che la mail non esista già
        if (utenteRepository.findByEmail(payload.email()).isPresent()) {
            throw new BadRequestException("L'email è già stata utilizzata.");
        }

        // Controllo che l'username non esista già
        if (utenteRepository.findByUsername(payload.username()).isPresent()) {
            throw new BadRequestException("L'username è già stato utilizzato.");
        }

        // Creazione utente
        Utente nuovoUtente = new Utente(
                payload.username(),
                payload.email(),
                passwordEncoder.encode(payload.password()),
                UtenteRuolo.USER
        );

        utenteRepository.save(nuovoUtente);

        return new UtenteResponse(
                nuovoUtente.getId(),
                nuovoUtente.getUsername(),
                nuovoUtente.getEmail(),
                nuovoUtente.getAvatar(),
                nuovoUtente.getDataRegistrazione(),
                nuovoUtente.getRuolo()
        );
    }
}

package elisaraeli.travelers_notebook_valley_backend.services;

import elisaraeli.travelers_notebook_valley_backend.entities.Utente;
import elisaraeli.travelers_notebook_valley_backend.entities.UtenteRuolo;
import elisaraeli.travelers_notebook_valley_backend.exceptions.BadRequestException;
import elisaraeli.travelers_notebook_valley_backend.exceptions.NotFoundException;
import elisaraeli.travelers_notebook_valley_backend.payloads.AdminDTO;
import elisaraeli.travelers_notebook_valley_backend.payloads.UtenteResponse;
import elisaraeli.travelers_notebook_valley_backend.payloads.UtenteUpdateDTO;
import elisaraeli.travelers_notebook_valley_backend.payloads.UtentiDTO;
import elisaraeli.travelers_notebook_valley_backend.repositories.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public List<UtenteResponse> findAll() {
        return utenteRepository.findAll()
                .stream()
                .map(u -> new UtenteResponse(
                        u.getId(),
                        u.getUsername(),
                        u.getEmail(),
                        u.getAvatar(),
                        u.getDataRegistrazione(),
                        u.getRuolo()
                ))
                .toList();
    }

    // Cerco l'utente per email
    public Utente findByEmail(String email) {
        return utenteRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("L'utente con questa email non è stato trovato."));
    }

    // Cerco l'utente per username
    public Utente findByUsername(String username) {
        return utenteRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("L'utente con questo username non è stato trovato."));
    }

    // Creo l'utente
    public UtenteResponse saveUtente(UtentiDTO payload) {

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

    // SOLO PER CREARE L'ADMIN
    public Utente createAdmin(AdminDTO body) {

        Utente admin = new Utente(
                body.username(),
                body.email(),
                passwordEncoder.encode(body.password())
        );

        admin.setRuolo(UtenteRuolo.ADMIN);

        return utenteRepository.save(admin);
    }

    // Cancello l'utente tramite id
    public void delete(UUID id) {
        Utente u = this.findById(id);
        utenteRepository.delete(u);
    }

    public UtenteResponse updateProfilo(UUID idUtente, UtenteUpdateDTO body) {
        Utente u = this.findById(idUtente);

        u.setUsername(body.username());
        u.setEmail(body.email());
        u.setAvatar(body.avatar());

        utenteRepository.save(u);

        return new UtenteResponse(
                u.getId(),
                u.getUsername(),
                u.getEmail(),
                u.getAvatar(),
                u.getDataRegistrazione(),
                u.getRuolo()
        );
    }


}

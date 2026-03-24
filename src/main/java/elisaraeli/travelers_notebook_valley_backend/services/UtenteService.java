package elisaraeli.travelers_notebook_valley_backend.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class UtenteService {

    private final UtenteRepository utenteRepository;
    private final PasswordEncoder passwordEncoder;
    private final MedagliaService medagliaService;
    private final Cloudinary cloudinary;


    @Autowired
    public UtenteService(UtenteRepository utenteRepository, PasswordEncoder passwordEncoder,
                         MedagliaService medagliaService, Cloudinary cloudinary
    ) {
        this.utenteRepository = utenteRepository;
        this.passwordEncoder = passwordEncoder;
        this.medagliaService = medagliaService;
        this.cloudinary = cloudinary;
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
        // assegno la medaglia all'utente per essersi registrato
        try {
            medagliaService.medagliaRegistrazione(nuovoUtente.getId());
        } catch (Exception e) {
            System.err.println("La medaglia di benvenuto non è stata assegnata: " + e.getMessage());
        }


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

    public Utente updateProfilo(UUID idUtente, UtenteUpdateDTO body) {
        Utente u = this.findById(idUtente);
        u.setUsername(body.username());
        u.setEmail(body.email());
        return utenteRepository.save(u);
    }

    // upload avatar
    public Utente uploadAvatar(UUID userId, MultipartFile file) {
        try {
            Utente u = this.findById(userId);

            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String url = uploadResult.get("secure_url").toString();

            u.setAvatar(url);
            utenteRepository.save(u);

            return u;

        } catch (IOException e) {
            throw new RuntimeException("Errore durante l'upload dell'avatar", e);
        }
    }
}

package elisaraeli.travelers_notebook_valley_backend.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import elisaraeli.travelers_notebook_valley_backend.entities.Conferita;
import elisaraeli.travelers_notebook_valley_backend.entities.Medaglia;
import elisaraeli.travelers_notebook_valley_backend.entities.Utente;
import elisaraeli.travelers_notebook_valley_backend.exceptions.BadRequestException;
import elisaraeli.travelers_notebook_valley_backend.exceptions.NotFoundException;
import elisaraeli.travelers_notebook_valley_backend.payloads.MedagliaDTO;
import elisaraeli.travelers_notebook_valley_backend.payloads.MedagliaResponse;
import elisaraeli.travelers_notebook_valley_backend.repositories.ConferitaRepository;
import elisaraeli.travelers_notebook_valley_backend.repositories.MedagliaRepository;
import elisaraeli.travelers_notebook_valley_backend.repositories.UtenteRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class MedagliaService {

    private final MedagliaRepository medagliaRepository;
    private final ConferitaRepository conferitaRepository;
    private final Cloudinary cloudinary;
    private final UtenteRepository utenteRepository;


    public MedagliaService(MedagliaRepository medagliaRepository, ConferitaRepository conferitaRepository,
                           Cloudinary cloudinary, UtenteRepository utenteRepository
    ) {
        this.medagliaRepository = medagliaRepository;
        this.conferitaRepository = conferitaRepository;
        this.cloudinary = cloudinary;
        this.utenteRepository = utenteRepository;

    }

    // CERCO LA MEDAGLIA PER ID
    public Medaglia findById(UUID id) {
        return medagliaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }

    // CREO LA MEDAGLIA
    public MedagliaResponse create(MedagliaDTO body) {

        // aggiungo un controllo per vedere se esiste già una medaglia con lo stesso nome
        medagliaRepository.findByNome(body.nome())
                .ifPresent(m -> {
                    throw new BadRequestException(
                            "Attenzione, è già presente una medaglia con il nome " + body.nome() + "."
                    );
                });


        Medaglia m = new Medaglia(
                body.nome(),
                body.descrizione(),
                body.icona()
        );

        medagliaRepository.save(m);

        return new MedagliaResponse(
                m.getId(),
                m.getNome(),
                m.getDescrizione(),
                m.getIcona(),
                null
        );
    }

    // MODIFICO LA MEDAGLIA
    public MedagliaResponse update(UUID id, MedagliaDTO body) {
        // cerco la medaglia per id
        Medaglia m = this.findById(id);
        // la modifico
        m.setNome(body.nome());
        m.setDescrizione(body.descrizione());
        m.setIcona(body.icona());

        // salvo
        medagliaRepository.save(m);
        return new MedagliaResponse(m.getId(), m.getNome(), m.getDescrizione(), m.getIcona(), null);
    }

    // ELIMINO LA MEDAGLIA
    public void delete(UUID id) {
        Medaglia m = this.findById(id);
        medagliaRepository.delete(m);
    }

    // Cerco le medaglie tramite id dell'utente
    public List<MedagliaResponse> getMedaglieByUtente(UUID idUtente) {
        return conferitaRepository.findByUtenteId(idUtente)
                .stream()
                .map(c -> new MedagliaResponse(
                        c.getMedaglia().getId(),
                        c.getMedaglia().getNome(),
                        c.getMedaglia().getDescrizione(),
                        c.getMedaglia().getIcona(),
                        c.getDataConferimento()
                ))
                .toList();
    }

    public List<MedagliaResponse> getAll() {
        return medagliaRepository.findAll()
                .stream()
                .map(m -> new MedagliaResponse(
                        m.getId(),
                        m.getNome(),
                        m.getDescrizione(),
                        m.getIcona(),
                        null
                ))
                .toList();
    }

    // per caricare l'icona della medaglia
    public MedagliaResponse uploadIcona(UUID id, MultipartFile file) {
        Medaglia m = this.findById(id);

        try {
            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "medaglie",
                            "public_id", "medaglia_" + id,
                            "overwrite", true
                    )
            );

            String url = uploadResult.get("secure_url").toString();

            m.setIcona(url);
            medagliaRepository.save(m);

            return new MedagliaResponse(
                    m.getId(),
                    m.getNome(),
                    m.getDescrizione(),
                    m.getIcona(),
                    null
            );

        } catch (IOException e) {
            throw new RuntimeException("Errore upload icona", e);
        }
    }

    // serve per assegnare la medaglia quando l'utente si registra
    public void medagliaRegistrazione(UUID utenteId) {
        Medaglia medaglia = medagliaRepository.findByNome("Benvenuto")
                .orElseThrow(() -> new RuntimeException("Ops, la medaglia non è stata trovata. Controlla bene il nome."));

        // controllo se l'utente ha già la medaglia
        boolean isConferita = conferitaRepository
                .existsByUtenteIdAndMedagliaId(utenteId, medaglia.getId());

        if (!isConferita) {

            // Recupero l’utente
            Utente utente = utenteRepository.findById(utenteId)
                    .orElseThrow(() -> new RuntimeException("Ops, l'utente non è stato trovato."));

            // Assegno la medaglia all'utente
            Conferita conferita = new Conferita(medaglia, utente);

            conferitaRepository.save(conferita);
        }
    }

}

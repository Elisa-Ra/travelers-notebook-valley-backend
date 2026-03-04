package elisaraeli.travelers_notebook_valley_backend.services;

import elisaraeli.travelers_notebook_valley_backend.entities.Conferita;
import elisaraeli.travelers_notebook_valley_backend.entities.Medaglia;
import elisaraeli.travelers_notebook_valley_backend.entities.Utente;
import elisaraeli.travelers_notebook_valley_backend.exceptions.NotFoundException;
import elisaraeli.travelers_notebook_valley_backend.payloads.ConferitaDTO;
import elisaraeli.travelers_notebook_valley_backend.payloads.ConferitaResponse;
import elisaraeli.travelers_notebook_valley_backend.payloads.MedagliaResponse;
import elisaraeli.travelers_notebook_valley_backend.repositories.ConferitaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

// SOLO L'ADMIN USA IL SERVICE DI CONFERITA (Solo l'admin può conferire medaglie)
@Service
public class ConferitaService {

    private final ConferitaRepository conferitaRepository;
    private final MedagliaService medagliaService;
    private final UtenteService utenteService;

    public ConferitaService(ConferitaRepository conferitaRepository, MedagliaService medagliaService, UtenteService utenteService) {
        this.conferitaRepository = conferitaRepository;
        this.medagliaService = medagliaService;
        this.utenteService = utenteService;
    }

    // CONFERISCO LA MEDAGLIA
    public ConferitaResponse assegnaMedaglia(ConferitaDTO body) {

        Medaglia medaglia = medagliaService.findById(body.idMedaglia());
        Utente utente = utenteService.findById(body.idUtente());

        Conferita conferita = new Conferita(medaglia, utente);
        conferitaRepository.save(conferita);

        return new ConferitaResponse(
                conferita.getId(),
                conferita.getDataConferimento(),
                medaglia.getId(),
                utente.getId()
        );
    }

    // PRENDO LA MEDAGLIA PER ID
    public ConferitaResponse getById(UUID id) {
        Conferita c = conferitaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));

        return new ConferitaResponse(
                c.getId(),
                c.getDataConferimento(),
                c.getMedaglia().getId(),
                c.getUtente().getId()
        );
    }

    // prendo le medaglie per id dell'utente
    public List<MedagliaResponse> getMedaglieByUtente(UUID idUtente) {
        return conferitaRepository.findByUtenteId(idUtente)
                .stream()
                .map(c -> new MedagliaResponse(
                        c.getMedaglia().getId(),
                        c.getMedaglia().getNome(),
                        c.getMedaglia().getDescrizione(),
                        c.getMedaglia().getIcona()
                ))
                .toList();
    }

}

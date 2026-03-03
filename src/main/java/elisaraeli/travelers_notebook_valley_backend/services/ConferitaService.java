package elisaraeli.travelers_notebook_valley_backend.services;

import elisaraeli.travelers_notebook_valley_backend.entities.Conferita;
import elisaraeli.travelers_notebook_valley_backend.entities.Medaglia;
import elisaraeli.travelers_notebook_valley_backend.entities.Utente;
import elisaraeli.travelers_notebook_valley_backend.payloads.ConferitaDTO;
import elisaraeli.travelers_notebook_valley_backend.payloads.ConferitaResponse;
import elisaraeli.travelers_notebook_valley_backend.repositories.ConferitaRepository;
import org.springframework.stereotype.Service;

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
}

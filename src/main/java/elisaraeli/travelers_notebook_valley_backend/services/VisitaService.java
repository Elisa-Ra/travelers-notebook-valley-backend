package elisaraeli.travelers_notebook_valley_backend.services;

import elisaraeli.travelers_notebook_valley_backend.entities.Monumento;
import elisaraeli.travelers_notebook_valley_backend.entities.Utente;
import elisaraeli.travelers_notebook_valley_backend.entities.Visita;
import elisaraeli.travelers_notebook_valley_backend.exceptions.NotFoundException;
import elisaraeli.travelers_notebook_valley_backend.payloads.VisitaDTO;
import elisaraeli.travelers_notebook_valley_backend.payloads.VisitaResponse;
import elisaraeli.travelers_notebook_valley_backend.repositories.VisitaRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

// L'utente può creare le visite
@Service
public class VisitaService {

    private final VisitaRepository visitaRepository;
    private final MonumentoService monumentoService;
    private final UtenteService utenteService;

    public VisitaService(VisitaRepository visitaRepository, MonumentoService monumentoService, UtenteService utenteService) {
        this.visitaRepository = visitaRepository;
        this.monumentoService = monumentoService;
        this.utenteService = utenteService;
    }

    // CREO LA VISITA
    public VisitaResponse registraVisita(VisitaDTO body, UUID idUtente) {

        Utente utente = utenteService.findById(idUtente);
        Monumento monumento = monumentoService.findById(body.idMonumento());

        Visita visita = new Visita(monumento, utente);
        visitaRepository.save(visita);

        return new VisitaResponse(
                visita.getId(),
                visita.getDataVisita(),
                monumento.getId(),
                utente.getId()
        );
    }

    // Cerco la visita per ID
    public VisitaResponse getById(UUID id) {
        Visita v = visitaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));

        return new VisitaResponse(
                v.getId(),
                v.getDataVisita(),
                v.getMonumento().getId(),
                v.getUtente().getId()
        );
    }

}

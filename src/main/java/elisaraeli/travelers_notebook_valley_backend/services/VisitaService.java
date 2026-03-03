package elisaraeli.travelers_notebook_valley_backend.services;

import elisaraeli.travelers_notebook_valley_backend.entities.Monumento;
import elisaraeli.travelers_notebook_valley_backend.entities.Utente;
import elisaraeli.travelers_notebook_valley_backend.entities.Visita;
import elisaraeli.travelers_notebook_valley_backend.payloads.VisitaDTO;
import elisaraeli.travelers_notebook_valley_backend.payloads.VisitaResponse;
import elisaraeli.travelers_notebook_valley_backend.repositories.VisitaRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

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
}

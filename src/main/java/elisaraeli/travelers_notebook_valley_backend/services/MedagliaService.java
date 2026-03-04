package elisaraeli.travelers_notebook_valley_backend.services;

import elisaraeli.travelers_notebook_valley_backend.entities.Medaglia;
import elisaraeli.travelers_notebook_valley_backend.exceptions.BadRequestException;
import elisaraeli.travelers_notebook_valley_backend.exceptions.NotFoundException;
import elisaraeli.travelers_notebook_valley_backend.payloads.MedagliaDTO;
import elisaraeli.travelers_notebook_valley_backend.payloads.MedagliaResponse;
import elisaraeli.travelers_notebook_valley_backend.repositories.ConferitaRepository;
import elisaraeli.travelers_notebook_valley_backend.repositories.MedagliaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MedagliaService {

    private final MedagliaRepository medagliaRepository;
    private final ConferitaRepository conferitaRepository;


    public MedagliaService(MedagliaRepository medagliaRepository, ConferitaRepository conferitaRepository) {
        this.medagliaRepository = medagliaRepository;
        this.conferitaRepository = conferitaRepository;
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
                m.getIcona()
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
        return new MedagliaResponse(m.getId(), m.getNome(), m.getDescrizione(), m.getIcona());
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
                        c.getMedaglia().getIcona()
                ))
                .toList();
    }


}

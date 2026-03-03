package elisaraeli.travelers_notebook_valley_backend.services;

import elisaraeli.travelers_notebook_valley_backend.entities.Medaglia;
import elisaraeli.travelers_notebook_valley_backend.exceptions.NotFoundException;
import elisaraeli.travelers_notebook_valley_backend.payloads.MedagliaDTO;
import elisaraeli.travelers_notebook_valley_backend.payloads.MedagliaResponse;
import elisaraeli.travelers_notebook_valley_backend.repositories.MedagliaRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MedagliaService {

    private final MedagliaRepository medagliaRepository;

    public MedagliaService(MedagliaRepository medagliaRepository) {
        this.medagliaRepository = medagliaRepository;
    }

    public Medaglia findById(UUID id) {
        return medagliaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }

    public MedagliaResponse create(MedagliaDTO body) {

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
}

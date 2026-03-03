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

    // CERCO LA MEDAGLIA PER ID
    public Medaglia findById(UUID id) {
        return medagliaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }

    // CREO LA MEDAGLIA
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

}

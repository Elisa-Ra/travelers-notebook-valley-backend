package elisaraeli.travelers_notebook_valley_backend.repositories;

import elisaraeli.travelers_notebook_valley_backend.entities.Conferita;
import elisaraeli.travelers_notebook_valley_backend.entities.Medaglia;
import elisaraeli.travelers_notebook_valley_backend.entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ConferitaRepository extends JpaRepository<Conferita, UUID> {
    List<Conferita> findByUtente(Utente utente);

    List<Conferita> findByMedaglia(Medaglia medaglia);
}


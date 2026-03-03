package elisaraeli.travelers_notebook_valley_backend.repositories;

import elisaraeli.travelers_notebook_valley_backend.entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UtenteRepository extends JpaRepository<Utente, UUID> {
    Optional<Utente> findByEmail(String email);

    Optional<Utente> findByUsername(String username);
}

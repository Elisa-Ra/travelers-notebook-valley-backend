package elisaraeli.travelers_notebook_valley_backend.repositories;

import elisaraeli.travelers_notebook_valley_backend.entities.Monumento;
import elisaraeli.travelers_notebook_valley_backend.entities.Post;
import elisaraeli.travelers_notebook_valley_backend.entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    List<Post> findByUtente(Utente utente);

    List<Post> findByMonumento(Monumento monumento);
}
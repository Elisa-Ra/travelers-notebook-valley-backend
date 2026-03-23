package elisaraeli.travelers_notebook_valley_backend.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "post")
@NoArgsConstructor
@Getter
@Setter
public class Post {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "id_utente", nullable = false)
    private Utente utente;

    @Column(nullable = false)
    private String titolo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contenuto;

    @Column(nullable = false, name = "data_creazione")
    private LocalDate dataCreazione = LocalDate.now();

    @Column(name = "data_modifica")
    private LocalDate dataModifica;

    @ManyToOne
    @JoinColumn(name = "id_monumento", nullable = false)
    private Monumento monumento;

    @Column(name = "foto_url")
    private String fotoUrl;

    public Post(Utente utente, String titolo, String contenuto, Monumento monumento) {
        this.utente = utente;
        this.titolo = titolo;
        this.contenuto = contenuto;
        this.monumento = monumento;
    }
}

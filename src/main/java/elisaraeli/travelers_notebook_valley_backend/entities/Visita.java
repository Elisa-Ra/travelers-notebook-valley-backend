package elisaraeli.travelers_notebook_valley_backend.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "visite")
@Getter
@Setter
@NoArgsConstructor
public class Visita {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(nullable = false, name = "data_visita")
    private LocalDate dataVisita = LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "id_monumento", nullable = false)
    private Monumento monumento;

    @ManyToOne
    @JoinColumn(name = "id_utente", nullable = false)
    private Utente utente;

    public Visita(Monumento monumento, Utente utente) {
        this.monumento = monumento;
        this.utente = utente;
    }
}

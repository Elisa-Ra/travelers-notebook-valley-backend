package elisaraeli.travelers_notebook_valley_backend.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "conferite")
@NoArgsConstructor
@Getter
@Setter
public class Conferita {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(nullable = false, name = "data_conferimento")
    private LocalDate dataConferimento = LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "id_medaglia", nullable = false)
    private Medaglia medaglia;

    @ManyToOne
    @JoinColumn(name = "id_utente", nullable = false)
    private Utente utente;

    public Conferita(Medaglia medaglia, Utente utente) {
        this.medaglia = medaglia;
        this.utente = utente;
    }
}

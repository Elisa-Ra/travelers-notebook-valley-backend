package elisaraeli.travelers_notebook_valley_backend.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "medaglie")
public class Medaglia {

    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String nome;

    @Column(nullable = false)
    private String descrizione;

    @Column(nullable = false)
    private String icona;
    @OneToOne
    @JoinColumn(name = "id_monumento")
    private Monumento monumento;


    public Medaglia(String nome, String descrizione, String icona) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.icona = icona;
    }
}

package dhbwka.wwi.vertysys.rest.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;


import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
public class Kunde extends RepresentationModel<Kunde> {

    public Kunde(){
        this.name=name;
        this.geschlecht=geschlecht;
        this.haarfarbe= haarfarbe;
        this.friseur=friseur;
    }
    @Id
    @NotNull(message = "Der Kunde muss eine ID haben")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn
    private Friseur friseur;

    @NotNull(message = "Der Kunde muss einem Namen haben")
    private String name;

    @NotNull(message = "Der Kunde muss ein Geschlecht haben")
    private String geschlecht;

    @NotNull(message = "Der Kunde muss eine haarfarbe haben")
    private String haarfarbe;


}

package dhbwka.wwi.vertysys.rest.entity;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Entity
@Getter
@Setter
public class Friseur extends RepresentationModel<Friseur> {


    public  Friseur(){
       this.name=name;
       this.geschlecht=geschlecht;
       this.adresse = adresse;

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY ) //hier muss für die SQL Auto befüllung von ID Identity gewählt werden nicht Auto
    @NotNull (message = "Die ID kann nicht null sein")
    private long id;

    @NotNull(message = "Der Friseur muss einen Namen haben")
    private String name;
    @NotNull(message = "Der Friseur muss ein geschlecht haben")
    private String geschlecht;
    @NotNull(message = "Der Friseur muss eine Adresse haben")
    private String adresse;


    @OneToMany (mappedBy = "friseur")
    private List<Kunde>kundeListe=new ArrayList<>();

    @OneToMany
    private Set<Kunde>stammKundenListe;



}

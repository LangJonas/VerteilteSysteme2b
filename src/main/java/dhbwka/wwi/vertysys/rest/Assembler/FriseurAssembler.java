package dhbwka.wwi.vertysys.rest.Assembler;

import dhbwka.wwi.vertysys.rest.controller.FriseurController;
import dhbwka.wwi.vertysys.rest.entity.Friseur;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static dhbwka.wwi.vertysys.rest.Assembler.KundeAssembler.*;

@Component
public class FriseurAssembler {
    //Methode die einem Iterable<Friseur> self Links hinzufügt
    public static   void friseurAddLink(Iterable<Friseur> friseure) {
        for (Friseur friseur : friseure) {
           friseurAddLink(friseur);
        }
    }

    //Methode die einem einzigen Friseur einen self Links hinzufügt
    public  static void friseurAddLink(Friseur friseur) {
        //Erstellen und hinzufügen des selfLinks
        Link  selfLink = linkTo(FriseurController.class).slash(friseur.getId()).withSelfRel();
        friseur.add(selfLink);

        //Erstellen und hinzufügen des Ddelete Links
        Link deleteLink = linkTo(FriseurController.class).slash(friseur.getId()).slash("delete").withRel("Friseur löschen");
        friseur.add(deleteLink);

        //Die Links zu allen Kunden des Friseurs hinzufügen
       kundeAddLink(  friseur.getKundeListe());
    }

    // Methode die die Friseur-Übersicht hinzufügt, im falle, dass nur ein einziger Friseur angezeigt wird
    public static void friseurAddÜbersicht(Friseur friseur){

        Link friseurÜbersicht = linkTo(FriseurController.class).withRel("Friseur Übersicht");
        friseur.add(friseurÜbersicht);
    }
}




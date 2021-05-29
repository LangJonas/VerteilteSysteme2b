package dhbwka.wwi.vertysys.rest.Assembler;

import dhbwka.wwi.vertysys.rest.controller.FriseurController;
import dhbwka.wwi.vertysys.rest.controller.KundeController;
import dhbwka.wwi.vertysys.rest.entity.Kunde;
import org.springframework.hateoas.Link;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class KundeAssembler {

    //Methode die einer Liste Kunden self Links zufügt. List wenn der Aufurf über Firseur geschieht
    public static void kundeAddLink(List<Kunde> kunden){
        for (Kunde kunde: kunden) {
            //Somit ist der Friseur ein Teil der URL
           kundeAddLinkMitFriserur(kunde);

        }

    }

    //Überladung der Obrigen Methode, dass sowohl Iterable als auch List Funktioniert. Iterable wird verwendet, wenn der aufruf über Kunden geschieht
    public static void kundeAddLink(Iterable<Kunde> kunden){
        for (Kunde kunde: kunden) {
            kundeAddLink(kunde);

        }

    }


    //Methode die einem einzigen Kunden mit self und delete Link zufügt
    public static void kundeAddLink(Kunde kunde){
            Link selfLink =linkTo(KundeController.class).slash(kunde.getId()).withSelfRel(); //auskommentiert , da ich mir nicht sicher bin wie ich es zurückgeben will
            kunde.add(selfLink);

        Link deleteLink = linkTo(KundeController.class).slash(kunde.getId()).slash("delete").withRel("Kunde löschen");
        kunde.add(deleteLink);

        }

    //Methode die einem einzigen Kunden mit seinen Links
    // Wenn über Friseur aufgerufen wird, wird der alternativLink mit Friseur angezeigt.
    public static void kundeAddLinkMitFriserur(Kunde kunde){
        Link selfLink =linkTo(FriseurController.class).slash(kunde.getFriseur().getId()).slash("kunden").slash(kunde.getId()).withSelfRel();
        kunde.add(selfLink);

        Link deleteLink = linkTo(FriseurController.class).slash(kunde.getFriseur().getId()).slash("kunden").slash(kunde.getId()).slash("delete").withRel("Kunde löschen");
        kunde.add(deleteLink);

    }

    //Link zur Kundenübersicht
    public static void  kundeAddÜbersichtLink (Kunde kunde){
        Link übersichtsLink = linkTo(KundeController.class).withRel("Kunden Übersichts Link");
        kunde.add(übersichtsLink);
    }

    //Friseurlink für den Fall, dass alle Kunden euines Friseurs angezeigt werden müssen
    public static void kundeAddFriseurLink(List<Kunde> kunden){
        //Für jeden Kunden den Link mit der Methode hinzufügen
       for (Kunde kunde: kunden)
           kundeAddFriseurLink(kunde);

    }

    //Hinzufügen des Friseurlinks zu einem einzigem Kunden.
    public static void kundeAddFriseurLink(Kunde kunde){
        Link friseurLink =linkTo(FriseurController.class).slash(kunde.getFriseur().getId()).withRel("Friseur Link");
        kunde.add(friseurLink);
    }


}




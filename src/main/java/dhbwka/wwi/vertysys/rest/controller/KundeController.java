package dhbwka.wwi.vertysys.rest.controller;

import dhbwka.wwi.vertysys.rest.entity.Friseur;
import dhbwka.wwi.vertysys.rest.entity.Kunde;
import dhbwka.wwi.vertysys.rest.repository.KundeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;



//static importsimport static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
//Damit die Klasse bei Methodenaufruf nicht genannt werden muss
import static dhbwka.wwi.vertysys.rest.Assembler.KundeAssembler.*;
import static dhbwka.wwi.vertysys.rest.Assembler.FriseurAssembler.*;


@CrossOrigin    //Damit die Anfrage aus 2c funktioniert

@RestController
@RequestMapping("/kunden")
@RequiredArgsConstructor
public class KundeController {
    private final KundeRepository kundeRepository;


    //Alle Kunden holen
    @GetMapping
    public Iterable<Kunde> fetchAllItems()
    {
        Iterable<Kunde> kunden = kundeRepository.findAll();
             kundeAddLink(kunden);
        return kunden;
    }

    // Einen spezifischen Kunden holen
    @GetMapping("/{id}")
    public EntityModel<Kunde> fetchItemById(@PathVariable long id)
    {
        Optional<Kunde> optionalKunde= kundeRepository.findById(id);
        Kunde kunde;
        //Wenn es kein Optional element gibt, wird Null zutückgegeben, sonst friseur abgespeichert
        if (optionalKunde.isPresent())
            kunde = optionalKunde.get();
        else return null;
        kundeAddLink( kunde);
        kundeAddFriseurLink(kunde);
        kundeAddÜbersichtLink(kunde);

        EntityModel<Kunde> result = new EntityModel(kunde) ;
        return result;
    }


    // Liefert den Friseur des Kunden mit der gegebenen Id
    @GetMapping("/{id}/friseur")
    public EntityModel<Friseur> fetchFriseurByKundeId(@PathVariable long id ) {
        Friseur friseur=  kundeRepository.findById(id).get().getFriseur();
        friseurAddLink(friseur);
        friseurAddÜbersicht(friseur);
        EntityModel<Friseur> result = new EntityModel(friseur) ;
        return result;
    }

    //neuen Kunden erstellen
    @PostMapping
    public Kunde createNewItem(@RequestBody Kunde kunde)
    {

        return kundeRepository.save(kunde);
    }


    //Kunden mit der gegebenen Id löschen
    @DeleteMapping("/{id}/delete")
    public void deleteItemById(@PathVariable long id)
    {
        kundeRepository.deleteById(id);

    }

    //Kunden mit der gegebenen ID löschen
    @PutMapping("/{idKunde}/update")
    private void updateItem(@RequestBody Kunde offer, @PathVariable long idKunde){

        Optional<Kunde> KundeAusID = kundeRepository.findById(idKunde);
        Kunde persisted;

        //Wenn das Optional Objekt gefüllt ist in persisted abspeichern, sonst return
        if (KundeAusID.isPresent())
            persisted = KundeAusID.get();
        else return ;

        //persisted mit den Daten aus offer updaten
        persisted.setName(offer.getName());
        persisted.setGeschlecht(offer.getGeschlecht());
        persisted.setHaarfarbe(offer.getHaarfarbe());
        persisted.setFriseur(offer.getFriseur());

        //persisted abspeichern
        kundeRepository.save(persisted);
    }



}

package dhbwka.wwi.vertysys.rest.controller;




import dhbwka.wwi.vertysys.rest.entity.Friseur;
import dhbwka.wwi.vertysys.rest.entity.Kunde;
import dhbwka.wwi.vertysys.rest.repository.FriseurRepository;
import dhbwka.wwi.vertysys.rest.repository.KundeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;


import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;


//Damit die Klasse bei Methodenaufruf nicht genannt werden muss
import static dhbwka.wwi.vertysys.rest.Assembler.KundeAssembler.*;
import static dhbwka.wwi.vertysys.rest.Assembler.FriseurAssembler.*;

@CrossOrigin //Damit die Anfrage aus 2c Funktioniert
@RestController
@RequestMapping("/friseure")
@RequiredArgsConstructor

public class FriseurController {

    private final FriseurRepository friseurRepository;
    private final KundeRepository kundeRepository;

    @GetMapping
    public CollectionModel<Friseur> fetchAllItems() {
        Iterable<Friseur> friseure = friseurRepository.findAll();
        System.out.println(friseure);

        //Hinzufügen der Self Links
        friseurAddLink(friseure);
        CollectionModel<Friseur> result = CollectionModel.of(friseure);
        System.out.println(result);
        return result;
    }


    //Liefert nur einen Friseur mit einer spezifischen IF
    @GetMapping("/{id}")
    public EntityModel<Friseur> fetchFriseurById(@PathVariable long id) {
        Optional<Friseur> optionalFriseur= friseurRepository.findById(id);
        Friseur friseur;
              //Wenn es kein Optional element gibt, wird Null zutückgegeben, sonst friseur abgespeichert
        if (optionalFriseur.isPresent())
            friseur = optionalFriseur.get();
        else return null;

            friseurAddLink( friseur);
            friseurAddÜbersicht(friseur);
            EntityModel<Friseur> result = new EntityModel(friseur);

        return result;
    }

    // Liefert alle Kunden eines Spezifischen Friseurs mit der gegebenen Id
    @GetMapping("/{id}/kunden")
    public CollectionModel<Kunde> fetchKundeByFriseurId(@PathVariable long id) {
        List<Kunde> kunden = friseurRepository.findById(id).get().getKundeListe();
        kundeAddLink(kunden);
        kundeAddFriseurLink(kunden);
        CollectionModel<Kunde> result = CollectionModel.of(kunden);
        return result;
    }

    //liefert den nten Kunden des Friseurs zurück
    @GetMapping("/{idFriseur}/kunden/{integerKunde}") //integer Kunde ist der wie vielte Kunde es in in der Liste ist. z.B. friseure/2/kunden/1 liefert den ersten kunden des zweiten friseurs aus. die Id des Kunden kann beliebig sein
        public EntityModel<Kunde> fetchKundeByIdByFriseur(@PathVariable long idFriseur, @PathVariable int integerKunde){

        List<Kunde> kunden = friseurRepository.findById(idFriseur).get().getKundeListe();

        Kunde kunde = kunden.get(integerKunde -1);
        kundeAddLinkMitFriserur(kunde);
        kundeAddFriseurLink(kunde);
        return new EntityModel(kunde);
        }

        //liefert die favourites eines Friseurs
    @GetMapping("/{id}/favourites")
    public Set<Kunde> fetchUserFavorits(@PathVariable long id){
        Optional<Friseur> friseurOptional = friseurRepository.findById(id);
        //Wenn es kein Optional element gibt, wird Null zutückgegeben, sonst friseur abgespeichert
        if (!friseurOptional.isPresent())
            return null;
        Friseur friseur = friseurOptional.get();

        friseurAddLink(friseur);

        return friseur.getStammKundenListe();
    }

    //erstellen eines Neuen Friseurs
    @PostMapping
    public Friseur createNewItem(@RequestBody Friseur friseur) {

        return friseurRepository.save(friseur);
    }

    //Einfügen eines neuen Kunden als Kunden des gegebenem Friseurs
    @PostMapping("/{id}/kunden")
    public Kunde createNewItem( @PathVariable long id, @RequestBody Kunde kunde) {
    Optional<Friseur> friseurAusID = friseurRepository.findById(id);
        //Wenn es kein Optional element gibt, wird Null zutückgegeben, sonst friseur abgespeichert
    Friseur friseur;
        if (friseurAusID.isPresent())
            friseur = friseurAusID.get();
        else return null;
    kunde.setFriseur(friseur);
    return kundeRepository.save(kunde);
    }

    //Der im Body gegebene Kunde wird zu den Favourites hinzugefügt
    @PostMapping("/{id}/favourites/add")
    public Friseur addStammkunde(@PathVariable long id, @RequestBody Kunde kunde){
        Optional<Friseur> result = friseurRepository.findById(id);
        //Wenn es kein Optional element gibt, wird Null zutückgegeben, sonst friseur abgespeichert
        if(!result.isPresent())
            return null;
        Friseur friseur = result.get();

        friseur.getStammKundenListe().add(kunde);
        return friseurRepository.save(friseur);
    }

    //Der im Body gegebene Kunde wird aus den Favourites entfernt
    @DeleteMapping("/{id}/favourites/remove")
    public void removeStammkunde(@PathVariable long id, @RequestBody Kunde kunde) {
        //Wenn es kein Optional element gibt, wird Null zutückgegeben, sonst friseur abgespeichert
        Optional<Friseur> result = friseurRepository.findById(id);
        if (!result.isPresent())
            return;
        Friseur friseur = result.get();
        friseur.getStammKundenListe().remove(kunde);
         friseurRepository.save(friseur);
    }

    //Löschen des Friseurs
    @DeleteMapping("/{id}/delete")
    public void deleteItemById(@PathVariable long id) {
        friseurRepository.deleteById(id);

    }

    //löscht den nten Kunden des Friseurs
    @DeleteMapping("/{idFriseur}/kunden/{integerKunde}/delete")
    public void deleteItemById(@PathVariable long idFriseur,@PathVariable int integerKunde) {
        List<Kunde> kunden = friseurRepository.findById(idFriseur).get().getKundeListe();
        Kunde kunde = kunden.get(integerKunde -1);

        if (kunde!= null)
            kundeRepository.deleteById(kunde.getId());
        else System.out.println("Jemand wollte einen nicht existenten Kunden Köschen");

    }

    //Der Friseur updaten
    @PutMapping("/{idFriseur}/update")
    private void updateItem(@RequestBody Friseur offer, @PathVariable long idFriseur) {
        //Wenn es kein Optional element gibt, wird Null zutückgegeben, sonst friseur abgespeichert
        Optional<Friseur> friseurAusID = friseurRepository.findById(idFriseur);
        Friseur persisted;
        if (friseurAusID.isPresent())
            persisted = friseurAusID.get();
        else return ;
        //Daten im Friseur ändern
        persisted.setName(offer.getName());
        persisted.setGeschlecht(offer.getGeschlecht());
        persisted.setAdresse(offer.getAdresse());

        //Daten im Friseur abspeichern
        friseurRepository.save(persisted);
    }

}






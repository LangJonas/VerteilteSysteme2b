package dhbwka.wwi.vertysys.rest.repository;


import dhbwka.wwi.vertysys.rest.entity.Friseur;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource (collectionResourceRel = "Friseure", path = "Friseur")
public interface FriseurRepository extends PagingAndSortingRepository<Friseur,Long> {
}

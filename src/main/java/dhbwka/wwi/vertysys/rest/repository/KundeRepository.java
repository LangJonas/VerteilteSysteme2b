package dhbwka.wwi.vertysys.rest.repository;

import dhbwka.wwi.vertysys.rest.entity.Kunde;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "kunde", path = "kunde")
public interface KundeRepository extends PagingAndSortingRepository<Kunde, Long> {
}

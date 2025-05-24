
package acme.realms;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface ManagerRepository extends AbstractRepository {

	@Query("select m from Manager m where m.identifierNumber = :identifierNumber")
	Manager findManagerByIdentifier(@Param("identifierNumber") String identifierNumber);
}

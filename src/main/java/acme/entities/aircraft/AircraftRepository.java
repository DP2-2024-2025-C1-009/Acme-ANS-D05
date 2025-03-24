
package acme.entities.aircraft;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface AircraftRepository extends AbstractRepository {

	@Query("SELECT COUNT(a) FROM Aircraft a WHERE a.numberRegistration = :numberRegistration")
	long countByNumberRegistration(@Param("numberRegistration") String numberRegistration);
}

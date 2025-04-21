
package acme.features.administrator.aircraft;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.aircraft.Aircraft;

@Repository
public interface AircraftRepository extends AbstractRepository {

	@Query("SELECT a from Aircraft a JOIN FETCH a.airline WHERE a.id = :id")
	Aircraft findAircraftById(int id);

	@Query("SELECT COUNT(a) FROM Aircraft a WHERE a.numberRegistration = :numberRegistration")
	long countByNumberRegistration(@Param("numberRegistration") String numberRegistration);

	@Query("SELECT a FROM Aircraft a")
	Collection<Aircraft> findAllAircrafts();

	@Query("SELECT a FROM Aircraft a WHERE a.model = :model")
	Aircraft findAircraftByModel(@Param("model") String model);

}

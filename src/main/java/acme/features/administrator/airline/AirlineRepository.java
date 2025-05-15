
package acme.features.administrator.airline;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.airline.Airline;

@Repository
public interface AirlineRepository extends AbstractRepository {

	@Query("select a from Airline a where a.id = :id")
	Airline findAirlineById(@Param("id") int id);

	@Query("select a from Airline a")
	Collection<Airline> findAllAirlines();

	@Query("select b from Airline b where b.iataCode =:codigo")
	Collection<Airline> findAllAirlineCode(@Param("codigo") String codigo);

	@Query("SELECT COUNT(a) FROM Airline a WHERE a.iataCode = :iataCode AND a.id <> :id")
	long countByIataCodeExcludingId(@Param("iataCode") String iataCode, @Param("id") int id);

}

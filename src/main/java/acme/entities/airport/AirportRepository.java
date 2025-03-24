
package acme.entities.airport;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface AirportRepository extends AbstractRepository {

	@Query("SELECT a.iataCode FROM Airport a")
	List<String> findAllAirportIataCodes();

	@Query("SELECT COUNT(a) FROM Airport a WHERE a.iataCode = :iataCode AND a.id <> :id")
	long countByIataCodeExcludingId(@Param("iataCode") String iataCode, @Param("id") int id);

}

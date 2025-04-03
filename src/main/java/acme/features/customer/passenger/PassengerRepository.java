
package acme.features.customer.passenger;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.Passenger;

@Repository
public interface PassengerRepository extends AbstractRepository {

	@Query("SELECT a from Passenger a where a.id = :id")
	Passenger findPassengerById(int id);

	@Query("SELECT a from Passenger a")
	Collection<Passenger> findAllPassengers();

	@Query("SELECT a from Passenger a where a.fullName = :fullName")
	Passenger FindPassengerByFullName(@Param("fullName") String fullName);

}

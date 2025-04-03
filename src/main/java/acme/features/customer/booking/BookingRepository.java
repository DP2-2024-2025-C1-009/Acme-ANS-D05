
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.Booking;

@Repository
public interface BookingRepository extends AbstractRepository {

	@Query("SELECT a from Booking a where a.id = :id")
	Booking findBookingById(int id);

	@Query("SELECT a from Booking a")
	Collection<Booking> findAllBookings();

	@Query("SELECT a from Booking a where a.locatorCode = :locatorCode")
	Booking findBookingByLocatorCode(@Param("locatorCode") String locatorCode);

}

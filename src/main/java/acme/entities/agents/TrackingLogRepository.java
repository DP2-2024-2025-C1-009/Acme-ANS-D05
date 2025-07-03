
package acme.entities.agents;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.datatypes.ClaimStatus;

@Repository
public interface TrackingLogRepository extends AbstractRepository {

	@Query("select t from TrackingLog t where t.trackingSteps.id = :claimId order by t.creationMoment asc")
	List<TrackingLog> findAllByClaimId(Integer claimId);

	@Query("select t from TrackingLog t where t.trackingSteps.id = :claimId and t.status = :status")
	List<TrackingLog> findAllByClaimIdAndStatus(Integer claimId, ClaimStatus status);
}

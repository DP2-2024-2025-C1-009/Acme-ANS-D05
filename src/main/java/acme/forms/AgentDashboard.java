
package acme.forms;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AgentDashboard extends AbstractForm {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	private Double				ratioSuccessfullyResolvedClaims;
	private Double				ratioRejectedClaims;
	private String				topThreeMonthsHighestNumberOfClaims;
	private Double				numberOfLogsClaimsHaveAverage;
	private Double				numberOfLogsClaimsHaveDeviation;
	private Integer				maximumnumberOfLogsClaimsHave;
	private Integer				minimumnumberOfLogsClaimsHave;
	private Double				numberOfClaimsAssistedLastMonthAverage;
	private Double				numberOfClaimsAssistedLastMonthDeviation;
	private Integer				maximumnumberOfClaimsAssistedLastMonth;
	private Integer				minimumnumberOfClaimsAssistedLastMonth;
}

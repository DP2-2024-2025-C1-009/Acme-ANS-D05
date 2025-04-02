
package acme.features.authenticated.flightCrewMember.flightAssignment;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.flightCrewMembers.FlightCrewMember;

@GuiController
public class CrewMemberFlightAssignmentController extends AbstractGuiController<FlightCrewMember, FlightAssignment> {

	// Servicios
	@Autowired
	private CrewMemberFlightAssignmentListCompletedService	listCompletedService;

	@Autowired
	private CrewMemberFlightAssignmentListPlannedService	listPlannedService;

	@Autowired
	private CrewMemberFlightAssignmentShowService			showService;

	@Autowired
	private CrewMemberFlightAssignmentCreateService			createService;

	@Autowired
	private CrewMemberFlightAssignmentUpdateService			updateService;

	@Autowired
	private CrewMemberFlightAssignmentPublishService		publishService;


	// Inicialización
	@PostConstruct
	protected void initialise() {
		super.addCustomCommand("list-completed", "list", this.listCompletedService);
		super.addCustomCommand("list-planned", "list", this.listPlannedService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addCustomCommand("publish", "update", this.publishService);
	}
}

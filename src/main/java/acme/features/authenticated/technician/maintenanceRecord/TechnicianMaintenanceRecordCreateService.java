
package acme.features.authenticated.technician.maintenanceRecord;

import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.maintenance.MaintenanceRecord;
import acme.entities.maintenance.Status;
import acme.realms.Technician;

@GuiService
public class TechnicianMaintenanceRecordCreateService extends AbstractGuiService<Technician, MaintenanceRecord> {

	@Autowired
	private TechnicianMaintenanceRecordRepository repository;


	@Override
	public void authorise() {
		boolean status;
		String method;
		int aircraftId;
		Aircraft aircraft;

		method = super.getRequest().getMethod();

		if (method.equals("GET"))
			status = true;
		else {
			aircraftId = super.getRequest().getData("aircraft", int.class);
			aircraft = this.repository.findAircraftById(aircraftId);
			status = aircraftId == 0 || aircraft != null && super.getRequest().getPrincipal().hasRealmOfType(Technician.class);
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		MaintenanceRecord maintenanceRecord;
		Technician technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();

		maintenanceRecord = new MaintenanceRecord();
		maintenanceRecord.setDraftMode(true);
		maintenanceRecord.setTechnician(technician);

		super.getBuffer().addData(maintenanceRecord);
	}

	@Override
	public void bind(final MaintenanceRecord maintenanceRecord) {
		Aircraft aircraft;
		int aircraftId;

		aircraftId = super.getRequest().getData("aircraft", int.class);
		aircraft = this.repository.findAircraftById(aircraftId);

		super.bindObject(maintenanceRecord, "ticker", "moment", "nextInspectionDueDate", "estimatedCost", "notes");
		maintenanceRecord.setStatus(Status.PENDING);
		maintenanceRecord.setAircraft(aircraft);
	}

	@Override
	public void validate(final MaintenanceRecord maintenanceRecord) {
		MaintenanceRecord existMaintenanceRecord;
		boolean validTicker;

		existMaintenanceRecord = this.repository.findMaintenanceRecordByTicker(maintenanceRecord.getTicker());
		validTicker = existMaintenanceRecord == null || existMaintenanceRecord.getId() == maintenanceRecord.getId();
		if (!validTicker)
			super.state(validTicker, "ticker", "acme.validation.task-record.ticker.duplicated.message");

		Date now = MomentHelper.getCurrentMoment();
		// Maintenance moment must be in the past
		boolean momentInPast = maintenanceRecord.getMoment() != null && MomentHelper.isBefore(maintenanceRecord.getMoment(), now);
		super.state(momentInPast, "moment", "acme.validation.maintenance-record.moment.past.message");
		//Next inspection must be in the future
		boolean nextInspectionInFuture = maintenanceRecord.getNextInspectionDueDate() != null && MomentHelper.isAfter(maintenanceRecord.getNextInspectionDueDate(), now);
		super.state(nextInspectionInFuture, "nextInspectionDueDate", "acme.validation.maintenance-record.next-inspection.future.message");

		if (momentInPast && nextInspectionInFuture) {
			Date minNext = MomentHelper.deltaFromMoment(maintenanceRecord.getMoment(), 1L, ChronoUnit.HOURS);
			boolean gapOk = MomentHelper.isAfterOrEqual(maintenanceRecord.getNextInspectionDueDate(), minNext);
			super.state(gapOk, "nextInspectionDueDate", "acme.validation.nextInspection.message");
		}
	}

	@Override
	public void perform(final MaintenanceRecord maintenanceRecord) {
		this.repository.save(maintenanceRecord);
	}

	@Override
	public void unbind(final MaintenanceRecord maintenanceRecord) {
		Dataset dataset;
		SelectChoices statusChoices;
		SelectChoices aircraftChoices;
		Collection<Aircraft> aircrafts;

		statusChoices = SelectChoices.from(Status.class, maintenanceRecord.getStatus());
		aircrafts = this.repository.findAllAircrafts();
		aircraftChoices = SelectChoices.from(aircrafts, "numberRegistration", maintenanceRecord.getAircraft());

		dataset = super.unbindObject(maintenanceRecord, "ticker", "moment", "nextInspectionDueDate", "estimatedCost", "notes", "draftMode");
		dataset.put("status", statusChoices.getSelected().getKey());
		dataset.put("statuses", statusChoices);
		dataset.put("aircraft", aircraftChoices.getSelected().getKey());
		dataset.put("aircrafts", aircraftChoices);

		super.getResponse().addData(dataset);
	}
}


package acme.features.authenticated.technician.maintenanceRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenance.MaintenanceRecord;
import acme.realms.Technician;

@GuiService
@Service
public class TechnicianMaintenanceRecordUpdateService extends AbstractGuiService<Technician, MaintenanceRecord> {

	@Autowired
	protected TechnicianMaintenanceRecordRepository repository;


	@Override
	public void authorise() {
		int id = super.getRequest().getData("id", int.class);
		MaintenanceRecord record = this.repository.findMaintenanceRecordById(id);
		boolean isDraft = record != null && record.isDraftMode();
		super.getResponse().setAuthorised(isDraft);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		MaintenanceRecord record = this.repository.findMaintenanceRecordById(id);
		super.getBuffer().addData(record);
	}

	@Override
	public void bind(final MaintenanceRecord record) {
		assert record != null;
		super.bindObject(record, "moment", "status", "nextInspectionDueDate", "estimatedCost", "notes");
		// Las relaciones technician y task no se actualizan aquí.
	}

	@Override
	public void validate(final MaintenanceRecord record) {
		// Validaciones adicionales si son necesarias.
	}

	@Override
	public void perform(final MaintenanceRecord record) {
		assert record != null;
		this.repository.save(record);
	}

	@Override
	public void unbind(final MaintenanceRecord record) {
		Dataset dataset = super.unbindObject(record, "moment", "status", "nextInspectionDueDate", "estimatedCost", "notes", "draftMode");
		dataset.put("confirmation", false);
		dataset.put("task", record.getTask());
		super.getResponse().addData(dataset);
	}
}

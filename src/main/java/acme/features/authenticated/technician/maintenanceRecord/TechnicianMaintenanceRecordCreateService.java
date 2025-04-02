
package acme.features.authenticated.technician.maintenanceRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenance.MaintenanceRecord;
import acme.realms.Technician;

@GuiService
@Service
public class TechnicianMaintenanceRecordCreateService extends AbstractGuiService<Technician, MaintenanceRecord> {

	@Autowired
	protected TechnicianMaintenanceRecordRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		MaintenanceRecord record = new MaintenanceRecord();
		record.setDraftMode(true);
		record.setMoment(MomentHelper.getCurrentMoment());
		// Se debe asignar la tarea; en el formulario se elegirá una tarea existente.
		// Aquí se deja null para que el binder la asocie.
		record.setTask(null);
		super.getBuffer().addData(record);
	}

	@Override
	public void bind(final MaintenanceRecord record) {
		assert record != null;
		super.bindObject(record, "moment", "status", "nextInspectionDueDate", "estimatedCost", "notes");
		record.setDraftMode(true);
		Technician technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();
		record.setTechnician(technician);
		// Se supone que la tarea se envía en el request. Asegúrate de que el formulario incluya el campo adecuado (por ejemplo, "task").
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
		// Si se requiere, incluir información de la tarea.
		dataset.put("task", record.getTask());
		super.getResponse().addData(dataset);
	}
}

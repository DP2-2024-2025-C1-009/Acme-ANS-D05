
package acme.features.authenticated.technician.maintenanceRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenance.MaintenanceRecord;
import acme.entities.maintenance.Status;
import acme.entities.maintenance.Task;
import acme.features.authenticated.technician.TechnicianTaskRepository;
import acme.realms.Technician;

@GuiService
public class TechnicianMaintenanceRecordShowService extends AbstractGuiService<Technician, MaintenanceRecord> {

	//Internal state ----------------------------------------------------------

	@Autowired
	private TechnicianMaintenanceRecordRepository	repository;

	@Autowired
	private TechnicianTaskRepository				taskRepository;

	//AbstractGuiService state ----------------------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);

	}

	@Override
	public void load() {
		MaintenanceRecord maintenanceRecord;
		int id = super.getRequest().getData("id", int.class);

		maintenanceRecord = this.repository.findMaintenanceRecordById(id);

		super.getBuffer().addData(maintenanceRecord);

	}

	@Override
	public void unbind(final MaintenanceRecord record) {
		// Se "desvinculan" los campos de MaintenanceRecord en un Dataset.
		Dataset dataset = super.unbindObject(record, "moment", "status", "nextInspectionDueDate", "estimatedCost", "notes", "draftMode");

		// Creamos un SelectChoices para el enum Status
		SelectChoices statusChoices = SelectChoices.from(Status.class, record.getStatus());
		dataset.put("status", statusChoices.getSelected().getKey());
		dataset.put("statusChoices", statusChoices);

		// Si se requiere, incluir otros campos (por ejemplo, la tarea)
		Collection<Task> publishedTasks = this.taskRepository.findPublishedTasks();
		SelectChoices taskChoices = SelectChoices.from(publishedTasks, "description", record.getTask());
		dataset.put("task", taskChoices.getSelected().getKey());
		dataset.put("taskChoices", taskChoices);

		dataset.put("confirmation", false);
		super.getResponse().addData(dataset);
	}
}

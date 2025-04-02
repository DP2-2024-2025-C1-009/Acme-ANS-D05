
package acme.features.authenticated.technician.maintenanceRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
@Service
public class TechnicianMaintenanceRecordPublishService extends AbstractGuiService<Technician, MaintenanceRecord> {

	@Autowired
	protected TechnicianMaintenanceRecordRepository	repository;

	@Autowired
	protected TechnicianTaskRepository				taskRepository;


	@Override
	public void authorise() {
		int id = super.getRequest().getData("id", int.class);
		MaintenanceRecord record = this.repository.findMaintenanceRecordById(id);
		boolean isOwner = record != null && super.getRequest().getPrincipal().hasRealm(record.getTechnician());
		boolean isDraft = record != null && record.isDraftMode();
		super.getResponse().setAuthorised(isOwner && isDraft);
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
		// Vincula los campos editables
		super.bindObject(record, "moment", "status", "nextInspectionDueDate", "estimatedCost", "notes");
	}

	@Override
	public void validate(final MaintenanceRecord record) {
		assert record != null;
		boolean confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation");

		// Validar que exista una tarea asociada
		Task task = record.getTask();
		super.state(task != null, "*", "acme.validation.maintenance-record.no-task");
		// Validar que la tarea esté publicada
		super.state(!task.isDraftMode(), "*", "acme.validation.maintenance-record.unpublished-task");
	}

	@Override
	public void perform(final MaintenanceRecord record) {
		assert record != null;
		record.setDraftMode(false);
		this.repository.save(record);
	}

	@Override
	public void unbind(final MaintenanceRecord record) {
		Dataset dataset = super.unbindObject(record, "moment", "status", "nextInspectionDueDate", "estimatedCost", "notes", "draftMode");
		// Creamos un SelectChoices para el enum Status
		SelectChoices statusChoices = SelectChoices.from(Status.class, record.getStatus());
		dataset.put("status", statusChoices.getSelected().getKey());
		dataset.put("statusChoices", statusChoices);

		// Creamos un SelectChoices para las tareas publicadas
		Collection<Task> publishedTasks = this.taskRepository.findPublishedTasks();
		SelectChoices taskChoices = SelectChoices.from(publishedTasks, "description", record.getTask());
		dataset.put("task", taskChoices.getSelected().getKey());
		dataset.put("taskChoices", taskChoices);

		dataset.put("confirmation", false);
		super.getResponse().addData(dataset);
	}
}

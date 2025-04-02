
package acme.features.authenticated.technician.maintenanceRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenance.MaintenanceRecord;
import acme.entities.maintenance.Status; // Asegúrate de importar el enum
import acme.entities.maintenance.Task;
import acme.features.authenticated.technician.TechnicianTaskRepository;
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
		// Ahora se vinculan también los datos del campo "task"
		super.bindObject(record, "moment", "status", "nextInspectionDueDate", "estimatedCost", "notes", "task");
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


	@Autowired
	private TechnicianTaskRepository taskRepository;


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

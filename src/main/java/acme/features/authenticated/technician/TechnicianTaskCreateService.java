
package acme.features.authenticated.technician;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenance.Task;
import acme.entities.maintenance.TaskType;
import acme.realms.Technician;

@GuiService
public class TechnicianTaskCreateService extends AbstractGuiService<Technician, Task> {

	@Autowired
	private TechnicianTaskRepository repository;


	@Override
	public void authorise() {
		//		boolean status;
		//		Task task;
		//		int masterId;
		//
		//		masterId = super.getRequest().getData("id", int.class);
		//		task = this.repository.findTaskById(masterId);
		//		status = task != null && super.getRequest().getPrincipal().hasRealmOfType(Technician.class)
		//			&& (task.getType() == TaskType.INSPECTION || task.getType() == TaskType.MAINTENANCE || task.getType() == TaskType.REPAIR || task.getType() == TaskType.SYSTEM_CHECK);
		//
		//		super.getResponse().setAuthorised(status);
		//
		boolean status;

		status = super.getRequest().getPrincipal().hasRealmOfType(Technician.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Task task;
		Technician technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();

		task = new Task();
		task.setType(TaskType.INSPECTION);
		task.setDraftMode(true);
		task.setTechnician(technician);

		super.getBuffer().addData(task);
	}

	@Override
	public void bind(final Task task) {

		super.bindObject(task, "ticker", "type", "description", "priority", "estimatedDuration");
	}

	@Override
	public void validate(final Task task) {
		Task existTask;
		boolean validTicker;

		existTask = this.repository.findTaskByTicker(task.getTicker());
		validTicker = existTask == null || existTask.getId() == task.getId();
		super.state(validTicker, "ticker", "acme.validation.task-record.ticker.duplicated.message");
	}

	@Override
	public void perform(final Task task) {
		this.repository.save(task);
	}

	@Override
	public void unbind(final Task task) {
		Dataset dataset;
		SelectChoices choices = SelectChoices.from(TaskType.class, task.getType());

		dataset = super.unbindObject(task, "ticker", "type", "description", "priority", "estimatedDuration", "draftMode");
		dataset.put("types", choices);

		super.getResponse().addData(dataset);
	}
}

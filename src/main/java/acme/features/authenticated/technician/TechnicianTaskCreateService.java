
package acme.features.authenticated.technician;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenance.Task;
import acme.entities.maintenance.TaskType;
import acme.realms.Technician;

@GuiService
@Service
public class TechnicianTaskCreateService extends AbstractGuiService<Technician, Task> {

	@Autowired
	protected TechnicianTaskRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Task task = new Task();
		task.setDraftMode(true);
		super.getBuffer().addData(task);
	}

	@Override
	public void bind(final Task task) {
		assert task != null;
		int accountId = super.getRequest().getPrincipal().getAccountId();
		Technician principal = this.repository.findTechnicianByAccountId(accountId);

		super.bindObject(task, "type", "description", "priority", "estimatedDuration");
		task.setTechnician(principal);
	}

	@Override
	public void validate(final Task task) {
		assert task != null;
		boolean confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation");
	}

	@Override
	public void perform(final Task task) {
		assert task != null;
		this.repository.save(task);
	}

	@Override
	public void unbind(final Task task) {
		assert task != null;
		Dataset dataset = super.unbindObject(task, "type", "description", "priority", "estimatedDuration", "draftMode", "technician");
		SelectChoices typeChoices = SelectChoices.from(TaskType.class, task.getType());
		dataset.put("type", typeChoices.getSelected().getKey());
		dataset.put("typeChoices", typeChoices);
		dataset.put("confirmation", false);
		super.getResponse().addData(dataset);
	}
}

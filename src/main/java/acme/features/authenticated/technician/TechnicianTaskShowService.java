
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
public class TechnicianTaskShowService extends AbstractGuiService<Technician, Task> {

	@Autowired
	protected TechnicianTaskRepository repository;


	@Override
	public void authorise() {
		int id = super.getRequest().getData("id", int.class);
		Task task = this.repository.findOneTaskById(id);
		boolean isOwner = task != null && super.getRequest().getPrincipal().hasRealm(task.getTechnician());
		super.getResponse().setAuthorised(isOwner);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		Task task = this.repository.findOneTaskById(id);
		super.getBuffer().addData(task);
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

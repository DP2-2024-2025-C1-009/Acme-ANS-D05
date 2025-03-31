
package acme.features.authenticated.maintenance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenance.Task;
import acme.realms.Technician;

@GuiService
@Service
public class TechnicianTaskPublishService extends AbstractGuiService<Technician, Task> {

	@Autowired
	protected TechnicianTaskRepository repository;


	@Override
	public void authorise() {
		int id = super.getRequest().getData("id", int.class);
		Task task = this.repository.findOneTaskById(id);
		boolean isOwner = task != null && super.getRequest().getPrincipal().hasRealm(task.getTechnician());
		boolean isDraft = task != null && task.isDraftMode();
		super.getResponse().setAuthorised(isOwner && isDraft);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		Task task = this.repository.findOneTaskById(id);
		super.getBuffer().addData(task);
	}

	@Override
	public void bind(final Task task) {
		assert task != null;
		super.bindObject(task, "type", "description", "priority", "estimatedDuration");
	}

	@Override
	public void validate(final Task task) {
		assert task != null;
		boolean confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation");
		// Aquí se podrían agregar validaciones adicionales específicas para la publicación.
	}

	@Override
	public void perform(final Task task) {
		assert task != null;
		task.setDraftMode(false);
		this.repository.save(task);
	}

	@Override
	public void unbind(final Task task) {
		assert task != null;
		Dataset dataset = super.unbindObject(task, "type", "description", "priority", "estimatedDuration", "draftMode", "technician");
		dataset.put("confirmation", false);
		super.getResponse().addData(dataset);
	}
}

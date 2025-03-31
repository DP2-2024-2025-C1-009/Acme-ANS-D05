
package acme.features.authenticated.maintenance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenance.Task;
import acme.realms.Technician;

@GuiService
@Service
public class TechnicianTaskDeleteService extends AbstractGuiService<Technician, Task> {

	@Autowired
	protected TechnicianTaskRepository repository;


	@Override
	public void authorise() {
		int id = super.getRequest().getData("id", int.class);
		Task task = this.repository.findOneTaskById(id);
		boolean isDraft = task != null && task.isDraftMode();
		super.getResponse().setAuthorised(task != null && isDraft);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		Task task = this.repository.findOneTaskById(id);
		super.getBuffer().addData(task);
	}

	@Override
	public void perform(final Task task) {
		assert task != null;
		this.repository.delete(task);
	}
	@Override
	public void bind(final Task task) {
		// No binding necessary for deletion.
	}

	@Override
	public void validate(final Task task) {
		// No validation necessary for deletion.
	}
	@Override
	public void unbind(final Task task) {
		// No unbinding necessary.
	}
}

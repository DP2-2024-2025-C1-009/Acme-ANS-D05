
package acme.features.authenticated.technician;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenance.Task;
import acme.realms.Technician;

@GuiService
public class TechnicianTaskListService extends AbstractGuiService<Technician, Task> {

	@Autowired
	protected TechnicianTaskRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int accountId = super.getRequest().getPrincipal().getAccountId();
		Technician principal = this.repository.findTechnicianByAccountId(accountId);
		int technicianId = principal.getId();
		Collection<Task> tasks = this.repository.findTasksByTechnicianId(technicianId);
		super.getBuffer().addData(tasks);
	}

	@Override
	public void unbind(final Task task) {
		assert task != null;
		Dataset dataset = super.unbindObject(task, "type", "description", "priority", "estimatedDuration", "draftMode", "technician");
		super.getResponse().addData(dataset);
	}
}

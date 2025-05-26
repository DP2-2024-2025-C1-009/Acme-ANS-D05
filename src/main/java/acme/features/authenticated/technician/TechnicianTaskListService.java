
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
	private TechnicianTaskRepository repository;


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Technician.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Task> object;
		int technicianId = super.getRequest().getPrincipal().getActiveRealm().getId();

		object = this.repository.findTasksByTechnicianId(technicianId);

		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final Task task) {
		Dataset dataset = super.unbindObject(task, "ticker", "type", "priority");
		super.addPayload(dataset, task, "description", "estimatedDuration");

		super.getResponse().addData(dataset);
	}
}

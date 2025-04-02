<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-moment code="technician.maintenance-record.form.label.moment" path="moment"/>
	<acme:input-textbox code="technician.maintenance-record.form.label.status" path="status"/>
	<acme:input-moment code="technician.maintenance-record.form.label.nextInsepctionDueDate" path="nextInspectionDueDate"/>
	<acme:input-money code="technician.maintenance-record.form.label.estimatedCost" path="estimatedCost"/>
	<acme:input-textbox code="technician.maintenance-record.form.label.notes" path="notes"/>
	<acme:input-textbox code="technician.maintenance-record.form.label.task" path="task"/>
	<acme:input-textbox code="technician.maintenance-record.form.label.technician" path="technician"/>
	
	
	<jstl:choose>	 
		<jstl:when test="${_command == 'show' && draftMode == false}">
			<acme:button code="technician.maintenance-record.form.button.tasks" action="/technician/task/list?maintenanceRecordId=${id}"/>			
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
			<acme:button code="technician.maintenance-record.form.button.tasks" action="/technician/task/list?maintenanceRecordId=${id}"/>
			<acme:submit code="technician.maintenance-record.form.button.update" action="/technician/maintenance-record/update"/>
			<acme:submit code="technician.maintenance-record.form.button.delete" action="/technician/maintenance-record/delete"/>
			<acme:submit code="technician.maintenance-record.form.button.publish" action="/technician/maintenance-record/publish"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="technician.maintenance-record.form.button.create" action="/technician/maintenance-record/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>
<%@ page %>

<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="acme" uri="http://acme-framework.org/" %>

<acme:form>
	<acme:input-select code="task.form.label.type" path="type" choices="${typeChoices}"/>
	<acme:input-textbox code="task.form.label.description" path="description"/>
	<acme:input-textbox code="task.form.label.priority" path="priority"/>
	<acme:input-textbox code="task.form.label.estimatedDuration" path="estimatedDuration"/>
	<jstl:choose>
		<jstl:when test="${_command == 'show' && draftMode == false}">
			<acme:button code="task.form.button.view" action="/technician/task/show?id=${id}"/>
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
			<acme:input-checkbox code="task.form.label.confirmation" path="confirmation"/>
			<acme:button code="task.form.button.view" action="/technician/task/show?id=${id}"/>
			<acme:submit code="task.form.button.update" action="/technician/task/update"/>
			<acme:submit code="task.form.button.delete" action="/technician/task/delete"/>
			<acme:submit code="task.form.button.publish" action="/technician/task/publish"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:input-checkbox code="task.form.label.confirmation" path="confirmation"/>
			<acme:submit code="task.form.button.create" action="/technician/task/create"/>
		</jstl:when>
	</jstl:choose>
</acme:form>

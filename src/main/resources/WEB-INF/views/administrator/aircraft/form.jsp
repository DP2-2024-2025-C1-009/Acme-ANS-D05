<%@page %>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="acme" uri="http://acme-framework.org/" %>

<acme:form>
	<acme:input-textbox code="aircraft.form.label.model" path="model" />
	<acme:input-textbox code="aircraft.form.label.number-registration" path="numberRegistration" placeholder="aircraft.form.placeholder.number-registration" />
	<acme:input-textbox code="aircraft.form.label.number-passengers" path="numberPassengers" />
	<acme:input-textbox code="aircraft.form.label.load-weight" path="loadWeight"  />
	<acme:input-checkbox code="aircraft.form.label.is-active" path="isActive" />
	<acme:input-textbox code="aircraft.form.label.optional-details" path="optionalDetails" />

	<jstl:choose>
		<jstl:when test="${_command == 'create'}">
			<acme:input-checkbox code="aircraft.form.label.confirmation" path="confirmation" />
			<acme:submit code="aircraft.form.button.create" action="/administrator/aircraft/create" />
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update')}">
			<acme:input-checkbox code="aircraft.form.label.confirmation" path="confirmation" />
			<acme:submit code="aircraft.form.button.update" action="/administrator/aircraft/update" />
		</jstl:when>
	</jstl:choose>
</acme:form>
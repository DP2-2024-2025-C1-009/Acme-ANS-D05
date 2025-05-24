<%@page %>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="acme" uri="http://acme-framework.org/" %>

<acme:form>
	<acme:input-textbox code="passenger.form.label.full-name" path="fullName" />
	<acme:input-textbox code="passenger.form.label.email" path="email"/>
	<acme:input-textbox code="passenger.form.label.passport" path="passport" />
	<acme:input-textbox code="passenger.form.label.birth-date" path="birthDate"  />
	<acme:input-textbox code="passenger.form.label.special-needs" path="specialNeeds" />
	
	<acme:input-checkbox code="passenger.form.label.confirmation" path="confirmation" />


	<jstl:choose>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="passenger.form.button.create" action="/customer/passenger/create" />
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update')}">
			<acme:submit code="passenger.form.button.update" action="/customer/passenger/update" />
		</jstl:when>
	</jstl:choose>
</acme:form>
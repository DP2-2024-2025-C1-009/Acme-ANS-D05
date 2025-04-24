<%@page %>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="acme" uri="http://acme-framework.org/" %>

<acme:form>
	<acme:input-textbox code="passenger.form.label.full-name" path="fullName" />
	<acme:input-textbox code="pasenger.form.label.email" path="email"/>
	<acme:input-textbox code="pasenger.form.label.passport" path="passport" />
	<acme:input-textbox code="pasenger.form.label.birth-date" path="birthDate"  />
	<acme:input-textbox code="pasenger.form.label.special-needs" path="specialNeeds" />


	<jstl:choose>
		<jstl:when test="${_command == 'create'}">
			<acme:input-checkbox code="pasenger.form.label.confirmation" path="confirmation" />
			<acme:submit code="pasenger.form.button.create" action="/customer/pasenger/create" />
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update')}">
			<acme:input-checkbox code="pasenger.form.label.confirmation" path="confirmation" />
			<acme:submit code="pasenger.form.button.update" action="/customer/pasenger/update" />
		</jstl:when>
	</jstl:choose>
</acme:form>
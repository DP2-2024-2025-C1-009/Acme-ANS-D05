<%@page %>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="acme" uri="http://acme-framework.org/" %>

<acme:list>
	<acme:list-column code="aircraft.list.label.model" path="model" width="10%" />
	<acme:list-column code="aircraft.list.label.number-registration" path="numberRegistration" width="20%" />
	<acme:list-column code="aircraft.list.label.airline" path="airline" width="20%" />
	<acme:list-column code="aircraft.list.label.is-active" path="isActive" width="10%" />

	<acme:list-payload path="payload" />
</acme:list>

<acme:button code="aircraft.list.button.new" action="/administrator/aircraft/create" />
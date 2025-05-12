<%@page %>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="acme" uri="http://acme-framework.org/" %>

<acme:list>
	<acme:list-column code="passenger.list.label.model" path="model" width="10%" />
	<acme:list-column code="passenger.list.label.number-registration" path="numberRegistration" width="20%" />
	<acme:list-column code="passenger.list.label.airline" path="airline" width="20%" />
	<acme:list-column code="passenger.list.label.is-active" path="isActive" width="10%" />


</acme:list>

<acme:button code="passenger.list.button.new" action="/customer/passenger/create" />
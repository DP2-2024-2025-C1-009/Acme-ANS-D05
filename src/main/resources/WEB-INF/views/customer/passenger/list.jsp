<%@page %>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="acme" uri="http://acme-framework.org/" %>

<acme:list>
	<acme:list-column code="passenger.list.label.full-name" path="fullName" width="20%" />
	<acme:list-column code="passenger.list.label.email" path="email" width="20%" />
	<acme:list-column code="passenger.list.label.passport" path="passport" width="20%" />
	<acme:list-column code="passenger.list.label.birth-date" path="birthDate" width="20%" />
	<acme:list-column code="passenger.list.label.special-needs" path="specialNeeds" width="20%" />
	
</acme:list>

<acme:button code="passenger.list.button.new" action="/customer/passenger/create" />

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="administrator.airport.label.name" path="airportName"/>
	<acme:list-column code="administrator.airport.label.iata" path="iataCode"/>
	<acme:list-column code="administrator.airport.label.city" path="city"/>
	<acme:list-column code="administrator.airport.label.country" path="country"/>
	<acme:list-column code="administrator.airport.label.website" path="website"/>
</acme:list>	

<jstl:if test="${_command == 'list'}">
	<acme:button code="administrator.airport.button.create" action="/administrator/airport/create"/>
</jstl:if>

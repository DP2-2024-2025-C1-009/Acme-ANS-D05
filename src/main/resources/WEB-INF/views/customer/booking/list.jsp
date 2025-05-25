<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="booking.list.label.locatorCode" path="locatorCode" width="40%"/>
	<acme:list-column code="booking.list.label.flightClass" path="flightClass" width="20%"/>
	<acme:list-column code="booking.list.label.cost" path="cost" width="30%"/>
</acme:list>

<jstl:if test="${_command == 'list'}">
	<acme:button code="booking.list.button.create" action="/customer/booking/create"/>
</jstl:if>
<%@page %>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="acme" uri="http://acme-framework.org/" %>

<acme:list>
	<acme:list-column code="booking.list.label.locator-code" path="locatorCode" width="10%" />
	<acme:list-column code="booking.list.label.purchase-time" path="purchaseTime" width="20%" />
	<acme:list-column code="booking.list.label.flight-class" path="flightClass" width="10%" />
	<acme:list-column code="booking.list.label.prize" path="prize" width="10%" />
	<acme:list-column code="booking.list.label.last-nibble" path="lastNibble" width="10%" />
</acme:list>

<acme:button code="booking.list.button.new" action="/customer/booking/create" />
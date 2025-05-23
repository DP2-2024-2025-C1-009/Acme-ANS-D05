<%@page %>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="acme" uri="http://acme-framework.org/" %>

<acme:form>
	<acme:input-textbox code="booking.form.label.locator-code" path="locatorCode" />
	<acme:input-textbox code="booking.form.label.purchase-time" path="purchaseTime" readonly="${purchaseTimeReadonly}" />
	<acme:input-select code="booking.form.label.flight-class" path="flightClass" choices="${flightclass}" />
	<acme:input-textbox code="booking.form.label.prize" path="prize"  />
	<acme:input-textbox code="booking.form.label.last-nibble" path="lastNibble" />
	
	<jstl:choose>
		<jstl:when test="${_command == 'create'}">
			<acme:input-checkbox code="booking.form.label.confirmation" path="confirmation" />
			<acme:submit code="booking.form.button.create" action="/customer/booking/create" />
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update')}">
			<acme:input-checkbox code="booking.form.label.confirmation" path="confirmation" />
			<acme:submit code="booking.form.button.update" action="/customer/booking/update" />
		</jstl:when>
	</jstl:choose>
</acme:form>

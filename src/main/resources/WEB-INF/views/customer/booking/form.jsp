<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-textbox code="booking.form.label.locatorCode" path="locatorCode"/>
	<acme:input-moment code="booking.form.label.purchaseTime" path="purchaseTime" readonly="true"/>
	<acme:input-select code="booking.form.label.flightClass" path="flightClass" choices="${classChoice}"/>
	<acme:input-textbox code="booking.form.label.prize" path="prize"/>
	<acme:input-textbox code="booking.form.label.lastCardNibble" path="lastCardNibble"/>
	<acme:input-money code="booking.form.label.cost" path="cost" readonly="true"/>
	<acme:input-select code="booking.form.label.flight" path="flight" choices="${flightChoice}"/>

	<jstl:choose>
		<jstl:when test="${_command == 'show'}">
			<acme:button code="booking.form.button.passengers" action="/customer/passenger/list-in-booking?masterId=${id}"/>
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update')}">
			<acme:button code="booking.form.button.passengers" action="/customer/passenger/list-in-booking?masterId=${id}"/>
			<acme:submit code="booking.form.button.update" action="/customer/booking/update"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="booking.form.button.create" action="/customer/booking/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>

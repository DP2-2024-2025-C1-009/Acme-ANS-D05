<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-textbox code="administrator.airport.label.name" path="airportName"/>
	<acme:input-textbox code="administrator.airport.label.iata" path="iataCode"/>	
	<acme:input-select code="administrator.airport.label.scope" path="operationalScope" choices="${operationalScope}"/>
	<acme:input-textbox code="administrator.airport.label.city" path="city"/>
	<acme:input-textbox code="administrator.airport.label.country" path="country"/>
	<acme:input-url code="administrator.airport.label.website" path="website"/>
	<acme:input-email code="administrator.airport.label.email" path="email"/>
	<acme:input-textbox code="administrator.airport.label.contactPhoneNumber" path="contactPhoneNumber"/>
	

	<jstl:choose>	 
		<jstl:when test="${_command == 'show'}">
			<acme:input-checkbox code="administrator.airport.form.label.confirmation" path="confirmation"/>
			<acme:submit code="administrator.airport.button.update" action="/administrator/airport/update"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:input-checkbox code="administrator.airport.form.label.confirmation" path="confirmation"/>
			<acme:submit code="administrator.airport.button.create" action="/administrator/airport/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>

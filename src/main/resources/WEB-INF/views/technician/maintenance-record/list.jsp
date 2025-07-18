<%@page %>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="technician.maintenance-record.list.label.ticker" path="ticker" width="15%"/>
	<acme:list-column code="technician.maintenance-record.list.label.moment" path="moment" width="15%"/>
	<acme:list-column code="technician.maintenance-record.list.label.status" path="status" width="15%"/>
	<acme:list-column code="technician.maintenance-record.list.label.next-inspection" path="nextInspectionDueDate" width="15%"/>
	<acme:list-column code="technician.maintenance-record.list.label.draftMode" path="draftMode" width="15%"/>
	<acme:list-payload path="payload"/>
</acme:list>

<jstl:if test="${_command == 'list'}">
	<acme:button code="technician.maintenance-record.list.button.create" action="/technician/maintenance-record/create"/>
</jstl:if>
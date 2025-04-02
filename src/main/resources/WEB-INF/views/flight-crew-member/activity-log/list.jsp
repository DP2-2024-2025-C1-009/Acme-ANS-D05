<%@page %>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="acme" uri="http://acme-framework.org/" %>

<acme:list>
	<acme:list-column code="activity-log.list.label.moment" path="registrationMoment" width="10%" />
	<acme:list-column code="activity-log.list.label.incident" path="incidentType" width="10%" />
	<acme:list-column code="activity-log.list.label.severity" path="severityLevel" width="10%" />
	<acme:list-column code="activity-log.list.label.status" path="draftMode" width="10%" />

	<acme:list-payload path="payload" />
</acme:list>

<acme:button code="activity-log.list.button.new" action="/flight-crew-member/activity-log/create?assignmentId=${assignmentId}" />

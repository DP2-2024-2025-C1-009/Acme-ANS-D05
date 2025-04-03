<%@page %>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="acme" uri="http://acme-framework.org/" %>

<acme:form>

    <acme:input-textbox 
        code="activity-log.form.label.incident-type" 
        path="incidentType" 
        readonly="draftMode"
        placeholder="activity-log.form.placeholder.incident-type" />

    <acme:input-textarea 
        code="activity-log.form.label.description" 
        path="description" 
        readonly="draftMode"
        placeholder="activity-log.form.placeholder.description" />

    <acme:input-textbox 
        code="activity-log.form.label.severity" 
        path="severityLevel" 
        readonly="draftMode"
        placeholder="activity-log.form.placeholder.severity" />

    <jstl:choose>
        <jstl:when test="${_command == 'create'}">
            <acme:submit code="activity-log.form.button.create" action="/flight-crew-member/activity-log/create?assignmentId=${assignmentId}" />
        </jstl:when>

        <jstl:when test="${acme:anyOf(_command, 'show|update') && draftMode == false}">
            <acme:submit code="activity-log.form.button.update" action="/flight-crew-member/activity-log/update" />
            <acme:submit code="activity-log.form.button.delete" action="/flight-crew-member/activity-log/delete" />
        </jstl:when>
    </jstl:choose>

    <jstl:if test="${acme:anyOf(_command, 'show|update') && draftMode == false && draftModeFlightAssignment == true}">
        <acme:submit code="activity-log.form.button.publish" action="/flight-crew-member/activity-log/publish" />
    </jstl:if>

</acme:form>

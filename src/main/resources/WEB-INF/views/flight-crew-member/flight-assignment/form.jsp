<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>

	<acme:input-select code="flight-crew-member.flight-assignment.form.label.duty"
					   path="duty" choices="${dutyChoices}"/>

	<acme:input-select code="flight-crew-member.flight-assignment.form.label.leg"
					   path="leg" choices="${legChoices}"/>

	<acme:input-textbox code="flight-crew-member.flight-assignment.form.label.crewMember"
						path="crewMember" readonly="true"/>

	<acme:input-moment code="flight-crew-member.flight-assignment.form.label.lastUpdate"
					   path="lastUpdate" readonly="true"/>

	<acme:input-select code="flight-crew-member.flight-assignment.form.label.assignmentStatus"
					   path="status" choices="${statusChoices}"/>

	<acme:input-textarea code="flight-crew-member.flight-assignment.form.label.remarks"
						 path="remarks"/>

	<jstl:choose>

		<jstl:when test="${_command == 'create'}">
			<acme:submit code="flight-crew-member.flight-assignment.form.button.create"
						 action="/flight-crew-member/flight-assignment/create"/>
		</jstl:when>

		<jstl:when test="${acme:anyOf(_command, 'show|update|publish') && draftMode && legNotCompleted}">
			<acme:button code="flight-crew-member.flight-assignment.form.button.activity-log"
						 action="/flight-crew-member/activity-log/list?masterId=${id}"/>
			<acme:submit code="flight-crew-member.flight-assignment.form.button.publish"
						 action="/flight-crew-member/flight-assignment/publish"/>
			<acme:submit code="flight-crew-member.flight-assignment.form.button.update"
						 action="/flight-crew-member/flight-assignment/update"/>
			<acme:submit code="flight-crew-member.flight-assignment.form.button.delete"
						 action="/flight-crew-member/flight-assignment/delete"/>
		</jstl:when>

		<jstl:when test="${acme:anyOf(_command, 'show|update|publish') && draftMode}">
			<acme:button code="flight-crew-member.flight-assignment.form.button.activity-log"
						 action="/flight-crew-member/activity-log/list?masterId=${id}"/>
		</jstl:when>

		<jstl:when test="${_command == 'show' && not draftMode}">
			<acme:button code="flight-crew-member.flight-assignment.form.button.activity-log"
						 action="/flight-crew-member/activity-log/list?masterId=${id}"/>
		</jstl:when>

	</jstl:choose>

</acme:form>

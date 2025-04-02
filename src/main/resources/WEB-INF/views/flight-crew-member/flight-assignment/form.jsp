<%@page %>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="acme" uri="http://acme-framework.org/" %>

<acme:form>

	<acme:input-select code="flight-assignment.form.label.duty" path="duty" choices="${dutyChoices}" />
	<acme:input-select code="flight-assignment.form.label.status" path="status" choices="${statusChoices}" />
	<acme:input-select code="flight-assignment.form.label.leg" path="leg" choices="${legs}" />
	<acme:input-textarea code="flight-assignment.form.label.remarks" path="remarks" />
	<jstl:if test="${_command == 'show'}">
	<jstl:if test="${not empty crewMembers}">
		<h3>Crew members assigned to this leg</h3>
			<ul>
				<jstl:forEach var="member" items="${crewMembers}">
					<li>
						<jstl:out value="${member.userAccount.identity.fullName}" />
					</li>
				</jstl:forEach>
			</ul>
		</jstl:if>
	</jstl:if>

	<jstl:choose>

		<jstl:when test="${_command == 'show' && draftMode == false}">
			<acme:button code="flight-assignment.form.button.view-leg" action="/flight-crew-member/flight-assignment/show?masterId=${id}" />
			<acme:button code="flight-assignment.form.button.view-crew" action="/flight-crew-member/flight-assignment/show?masterId=${id}" />
		</jstl:when>

		<jstl:when test="${acme:anyOf(_command, 'show|update|publish') && draftMode == true}">
			<acme:input-checkbox code="flight-assignment.form.label.confirmation" path="confirmation" />
			<acme:button code="flight-assignment.form.button.view-leg" action="/flight-crew-member/flight-assignment/show?masterId=${id}" />
			<acme:button code="flight-assignment.form.button.view-crew" action="/flight-crew-member/flight-assignment/show?masterId=${id}" />
			<acme:submit code="flight-assignment.form.button.update" action="/flight-crew-member/flight-assignment/update" />
			<acme:submit code="flight-assignment.form.button.publish" action="/flight-crew-member/flight-assignment/publish" />
		</jstl:when>

		<jstl:when test="${_command == 'create'}">
			<acme:input-checkbox code="flight-assignment.form.label.confirmation" path="confirmation" />
			<acme:submit code="flight-assignment.form.button.create" action="/flight-crew-member/flight-assignment/create" />
		</jstl:when>

	</jstl:choose>

</acme:form>

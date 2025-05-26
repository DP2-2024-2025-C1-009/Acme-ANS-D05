<%--
- form.jsp
-
- Copyright (C) 2012-2025 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="any.service.form.label.name" path="name"/>
	<acme:input-url code="any.service.form.label.photo-link" path="photoLink"/>
	<acme:input-double code="any.service.form.label.average-dwell-time" path="averageDwellTime"/>
	<acme:input-textbox code="any.service.form.label.promotion-code" path="promotionCode"/>
	<acme:input-money code="any.service.form.label.discounted-money" path="discountedMoney"/>
</acme:form>
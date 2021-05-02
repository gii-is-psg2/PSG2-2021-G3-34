<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="adoptions">
	<div class="col-sm-offset-11">
			<a href="<spring:url value="/adoptions/pendingAdoptionsList" htmlEscape="true" />">
			<button class="btn btn-default" type="submit">Ver aplicaciones de adopción pendientes</button>
			</a>
	</div>
	<h2>
		Mascotas en adopción
	</h2>
	<table id="adoptionsTable" class="table table-striped">
		<thead>
			<tr style="background-color: #f1f1f1">

				<th>Dueño</th>
				<th>Tipo de mascota</th>
				<th>Mascota</th>
				<th>Adopcion</th>

				<%--  <th>Actions</th> --%>

			</tr>
		</thead>
		<tbody>
			<c:forEach items="${pets}" var="pet">
				<tr>
					<td><c:out value="${pet.owner.firstName}" /></td>
					<td><c:out value="${pet.type}" /></td>
					<td><c:out value="${pet.name} con identificador:${pet.id}" />
					</td>
					<td><c:if test="${possibleOwner!=pet.owner}">
							<spring:url value="/adoptions/{petId}/applicationForm"
								var="applicationFormUrl">
								<spring:param name="petId" value="${pet.id}" />
							</spring:url>
							<a href="${fn:escapeXml(applicationFormUrl)}"
								class="btn btn-default">Formulario</a>
						</c:if></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</petclinic:layout>
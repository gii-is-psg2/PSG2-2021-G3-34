<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="adoptions">
	<div class="col-sm-offset-11">
			<a href="<spring:url value="${alternativa}" htmlEscape="true" />">
			<button class="btn btn-default" type="submit"><c:out value = "${boton}" /></button>
			</a>
	</div>
    <h2> <c:out value="${mensaje}"/></h2>

    <table id="adoptionsTable" class="table table-striped">
        <thead>
        <tr style = "background-color: #f1f1f1">

            <th>Dueño</th>
            <th>Dueño posible</th>
            <th>Descripción</th>
            <th>Mascota</th>
            <th>Estado de la adopción</th>
            <th>Acciones</th>

          <%--  <th>Actions</th> --%>

        </tr>
        </thead>
        <tbody>
        <c:forEach items="${adoptions}" var="adoption">
            <tr>
            	<td>
                    <c:out value="${adoption.owner}"/>
                </td>
                <td>
                    <c:out value="${adoption.possibleOwner}"/>
                </td>
                <td>
                    <c:out value="${adoption.description}"/>
                </td>
                <td>
                    <c:out value="${adoption.pet.name} con identificador:${adoption.pet.id}"/>
                </td>
                <td>
                    <c:out value="${adoption.adoptionStateType}"/>
                </td>
                <td><c:if test="${pendingAdoption==adoption.adoptionStateType}">
                <spring:url value="/adoptions/deny/${adoption.id}" var="denyAdoptionApplication">
                </spring:url>
                <a href="${fn:escapeXml(denyAdoptionApplication)}" class="glyphicon glyphicon-remove-circle"></a>
                
                <spring:url value="/adoptions/accept/${adoption.id}" var="acceptAdoptionApplication">
                </spring:url>
                <a href="${fn:escapeXml(acceptAdoptionApplication)}" class="glyphicon glyphicon-ok-circle"></a>
                
                
                </c:if></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    
    
</petclinic:layout> 
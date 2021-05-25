<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<petclinic:layout pageName="owners">

	<c:out value="${message}"></c:out>

    <h2>Owner Information</h2>


    <table class="table table-striped">
        <tr>
            <th>Nombre completo</th>
            <td><b><c:out value="${owner.firstName} ${owner.lastName}"/></b></td>
        </tr>
        <tr>
            <th>Dirección</th>
            <td><c:out value="${owner.address}"/></td>
        </tr>
        <tr>
            <th>Ciudad</th>
            <td><c:out value="${owner.city}"/></td>
        </tr>
        <tr>
            <th>Telefono</th>
            <td><c:out value="${owner.telephone}"/></td>
        </tr>
    </table>

    <spring:url value="{ownerId}/edit" var="editUrl">
        <spring:param name="ownerId" value="${owner.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Editar Dueño</a>

    <spring:url value="{ownerId}/pets/new" var="addUrl">
        <spring:param name="ownerId" value="${owner.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(addUrl)}" class="btn btn-default">Añadir Nueva Mascota</a>

    <br/>
    <br/>
    <br/>
    <h2>Mascotas, Visitas y Alojamientos</h2>

    <table class="table table-striped">
        <c:forEach var="pet" items="${owner.pets}">

            <tr>
                <td valign="top">
                    <dl class="dl-horizontal">
                        <dt>Nombre</dt>
                        <dd><c:out value="${pet.name}"/></dd>
                        <dt>Fecha de Nacimiento</dt>
                        <dd><petclinic:localDate date="${pet.birthDate}" pattern="yyyy-MM-dd"/></dd>
                        <dt>Tipo</dt>
                        <dd><c:out value="${pet.type.name}"/></dd>
                    </dl>
                </td>
                <td valign="top">
                    <table class="table-condensed">
                        <thead>
                        <tr>
                            <th>Fecha de Visita</th>
                            <th>Descripcion</th>
                        </tr>
                        </thead>
                        <c:forEach var="visit" items="${pet.visits}">
                            <tr>
                                <td><petclinic:localDate date="${visit.date}" pattern="yyyy-MM-dd"/></td>
                                <td><c:out value="${visit.description}"/></td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td>
                                <spring:url value="/owners/{ownerId}/pets/{petId}/edit" var="petUrl">
                                    <spring:param name="ownerId" value="${owner.id}"/>
                                    <spring:param name="petId" value="${pet.id}"/>
                                </spring:url>
                                <a href="${fn:escapeXml(petUrl)}">Editar Mascota</a>
                            </td>
                            <td>
                                <spring:url value="/owners/{ownerId}/pets/{petId}/visits/new" var="visitUrl">
                                    <spring:param name="ownerId" value="${owner.id}"/>
                                    <spring:param name="petId" value="${pet.id}"/>
                                </spring:url>
                                <a href="${fn:escapeXml(visitUrl)}">Añadir Visita</a>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>

        </c:forEach>
    </table>
	<h2>Reservas hotel</h2>
	  
    <table id="bookingsTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 120px;">Fecha de Inicio</th>
            <th style="width: 120px;">Fecha de Fin</th>
            <th style="width: 100px">Habitación</th>
            <th style="width: 140px">Mascota</th>
            <th style="width: 30px"></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${owner.pets}" var="petsOwner">
        	<c:forEach items="${petsOwner.bookings}" var="booking">
            <tr>
               
                <td>
                    <c:out value="${booking.startDate}"/>
                </td>
                <td>
                    <c:out value="${booking.endDate}"/>
                </td>
                <td>
                    <c:out value="${booking.room}"/>
                </td>
                <td>
                    <c:out value="${booking.pet.name}"/>
                </td>
                <td>
               	<sec:authorize access="hasAuthority('admin')">
                    <spring:url value="/owners/{ownerId}/bookings/{bookingId}/delete" var="deleteUrl">
						<spring:param name="ownerId" value="${owner.id}"/>
						<spring:param name="bookingId" value="${booking.id}"/>
					</spring:url>
                    <a href="${fn:escapeXml(deleteUrl)}" class="btn btn-danger">Borrar</a>
                </sec:authorize>
                </td>
              
            </tr>
            </c:forEach>
        </c:forEach>
        </tbody>
    </table>
    <spring:url value="/owners/{ownerId}/bookings/new" var="newBookingUrl">
    	<spring:param name="ownerId" value="${owner.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(newBookingUrl)}" class="btn btn-default">Nueva Reserva</a>
	
</petclinic:layout>

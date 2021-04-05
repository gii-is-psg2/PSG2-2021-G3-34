<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>


<petclinic:layout pageName="bookings">
    <jsp:attribute name="customScript">
        <script>
            $(function () {
                $("#startDate").datepicker({dateFormat: 'yy/mm/dd'});
                $("#endDate").datepicker({dateFormat: 'yy/mm/dd'});
            });
        </script>
    </jsp:attribute>
    <jsp:body>
        <h2><c:if test="${booking['new']}">Nueva </c:if>Reserva</h2>

        <form:form modelAttribute="booking" class="form-horizontal">
            <div class="form-group has-feedback">
                <petclinic:inputField label="Fecha de inicio" name="startDate"/>
                <petclinic:inputField label="Fecha de fin" name="endDate"/>
                <div class="control-group">
                    <petclinic:selectField name="pet.name" label="Nombre de la mascota " names="${petsNames}" size="5"/>
                </div>
            </div>

            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <input type="hidden" name="petId" value="${booking.pet.id}"/>
                    <button class="btn btn-default" type="submit">Añadir reserva</button>
                </div>
            </div>
        </form:form>

        <br/>
        <b>Reservas anteriores</b>
        <table id="bookingsTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 150px;">Fecha de inicio</th>
            <th style="width: 150px;">Fecha de fin</th>
            <th style="width: 80px">Habitación</th>
            <th style="width: 80px">Mascota</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${owner.pets}" var="petsOwner">
        	<c:forEach items="${petsOwner.bookings}" var="bookingPrev">
            <tr>
               
                <td>
                    <c:out value="${bookingPrev.startDate}"/>
                </td>
                <td>
                    <c:out value="${bookingPrev.endDate}"/>
                </td>
                <td>
                    <c:out value="${bookingPrev.room}"/>
                </td>
                <td>
                    <c:out value="${bookingPrev.pet.name}"/>
                </td>
                
            </tr>
            </c:forEach>
        </c:forEach>
        </tbody>
    </table>
    </jsp:body>
</petclinic:layout>


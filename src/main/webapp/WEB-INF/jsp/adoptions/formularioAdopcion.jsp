<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<petclinic:layout pageName="adoptions">
    
    <jsp:body>
	   
	    <h2><fmt:message key="label.adoption.formTitle"/></h2>
	    
	    <form:form modelAttribute="adoption" class="form-horizontal">
	        <div class="form-group has-feedback">
                <div class="form-group">
                    <label class="col-sm-2 control-label"><fmt:message key="label.adoption.owner"/></label>
                    <div class="col-sm-10">
                        <c:out value="${originalOwner}"/>
                    </div>
                </div>
	        </div>
	        <div class="form-group has-feedback">
                <div class="form-group">
                    <label class="col-sm-2 control-label"><fmt:message key="label.adoption.newOwner"/></label>
                    <div class="col-sm-10">
                        <c:out value="${possibleOwner}"/>
                    </div>
                </div>
	        </div>
	        <input type="hidden" name="owner" value="${originalOwner}"/>
	        <input type="hidden" name="possibleOwner" value="${possibleOwner}"/>
	        <petclinic:inputField label="Description" name="description"/>
	        <div class="form-group">
	            <div class="col-sm-offset-2 col-sm-10">
	            	<button class="btn btn-default" type="submit"><fmt:message key="label.adoption.send"/></button>
	            </div>
	        </div>
	    </form:form>
	   
    </jsp:body>
	
</petclinic:layout>
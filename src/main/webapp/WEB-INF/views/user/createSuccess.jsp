<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="pageTitle" scope="request" value="createSuccess"/>

<%@ include file="../header.jsp" %>
<div id="successPanel">

        <c:choose>
            <c:when test="${ hasError }">
            <p class="prompt-error">
                ${errors.username} ${errors.password}
            </p>
            </c:when>
            <c:otherwise>
            <p class="prompt">
                Woohoo, User <span class="username">${user.userName}</span> has been created successfully!
            </p>
            </c:otherwise>
        </c:choose>

</div>

<%@ include file="../footer.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" scope="request" value="createPost"/>

<%@ include file="../header.jsp" %>


    <c:choose>
        <c:when test="${not empty error}">
            <div id="createError" class="page-action create-error">
                Title or content cannot be empty!
                ${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}
            </div>
        </c:when>
        <c:otherwise>
            <div id="createHint" class="page-action">

            </div>
        </c:otherwise>
    </c:choose>

<div id="createPanel">
    <form class="form-horizontal" action="<c:url value='/posts/create' />" method="post">
        <div class="control-group">
            <label class="control-label" for="title">Title</label>
            <div class="controls">
                <input class="form-control" type="text" placeholder="post title" id="title" name="title" />
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="content">Content</label>
            <div class="controls">
                <textarea class="form-control" placeholder="post content" id="content" name="content" rows="6"></textarea>
            </div>
        </div>
        <div class="control-group">
            <div class="controls">
                <button type="submit" class="btn btn-primary">Create</button>
            </div>
        </div>
    </form>
</div>

<%@ include file="../footer.jsp" %>
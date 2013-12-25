<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" scope="request" value="createPost"/>

    <script type="text/javascript"></script>
    <script>
    function showTags()
    {
        var cont = document.getElementById('tags').value;
        $("#tagLabel").text(cont);
        document.createForm.allTags.value=cont;
    }
    </script>

<body>
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
    <form class="form-horizontal" name="createForm" action="<c:url value='/posts/create' />" method="post">
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
            <div>
                <label class="control-label">Tags:</label>
                <label id="tagLabel" class="control-label"></label>
            </div>
            <div>
                <input id="tags" class="form-control" placeholder="post tags"/>
                <button type="button" class="btn btn-default" id="addButton" onclick="showTags();">Add</button>
                <input type="hidden" id="allTags" name="allTags">
            </div>
        </div>
        <div class="control-group">
            <div class="controls">
                <button type="submit" class="btn btn-primary">Create</button>
            </div>
        </div>
</div>




<%@ include file="../footer.jsp" %>
</body>

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
                <input type="text" placeholder="post title" id="title" name="title" />
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="content">Content</label>
            <div class="controls">
                <textarea placeholder="post content" id="content" name="content" rows="6"></textarea>
            </div>
        </div>
        <div class="control-group">
            <div>
            <label style="align-left"><span>Tags:<span></label>
            <label id="tagLabel"><span></span></label>
            </div>
            <div>
                <textarea placeholder="post tags" id="tags" rows="1"></textarea>
                <button type="button" class="btn" id="addButton" onclick="showTags();">Add</button>
                <input type="hidden" id="allTags" name="allTags">
            </div>
        </div>
        <div class="control-group">
            <div class="controls">
                <button type="submit" class="btn">Create</button>
            </div>
        </div>
</div>




<%@ include file="../footer.jsp" %>
</body>
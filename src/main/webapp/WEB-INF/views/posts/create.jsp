<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" scope="request" value="createPost"/>

    <script type="text/javascript"></script>
    <script>
    function showTags()
    {
        var cont = document.getElementById('tags').value;
        var pre = $("#tagLabel").text();
        var after = cont.concat(";", pre);
        var tags = after.split(/;|,/);
        tags = tags.filter (function (v, i, a) { return a.indexOf (v) == i });

        var len = document.getElementById("tagsLabel").childNodes.length;
        var s = new Array(len);
        for(var j=0;j<len;j++)
        {
            s[j] = document.getElementById("tagsLabel").childNodes[j].innerHTML;
        }

        for(var i=0;i<tags.length;i++)
        {
            if("" != tags[i] )
            {
                var flag = false;
                for(var k=0; k<s.length; k++)
                {
                    if(s[k] == tags[i])
                    {
                        flag = true;
                    }
                }
                if(flag == false)
                {
                    var label = $("<label style='color: #4262A1; background: white;margin: 0 5px 0 0 ; width: auto; border-left: white solid 3px; border-right: white solid 3px;border-radius: 5px;'>").text(tags[i] .replace(/(^\s*)|(\s*$)/g, ""));
                    $("#tagsLabel").append(label);
                }

            }
        }

        document.createForm.allTags.value=tags.join();
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
            <c:if test="${not empty illegal}">
                <div id="createHint" class="page-action create-error">${illegal}</div>
            </c:if>
        </c:otherwise>
    </c:choose>

<div id="createPanel">
    <form class="form-horizontal post-create" name="createForm" action="<c:url value='/posts/create' />" method="post">
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
                <div id="tagsLabel"></div>
            </div>
            <div>
                <input id="tags" class="form-control" placeholder="post tags"/>
                <button type="button" class="btn btn-default" id="addButton" onclick="showTags();">Add</button>
                <input type="hidden" id="allTags" name="allTags">
            </div>
        </div>
        <div class="control-group">
            <div class="controls">
                <button type="submit" class="btn btn-primary" id="create">Create</button>
            </div>
        </div>
</div>



<script type="text/javascript" src="<c:url value='/scripts/violations.js' />"></script>
<%@ include file="../footer.jsp" %>
</body>

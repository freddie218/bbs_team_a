<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" scope="request" value="Home"/>

<%@ include file="header.jsp" %>

<script type="text/javascript">
    function getURLParameter(name) {
        return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search)||[,""])[1].replace(/\+/g, '%20'))||null;
    }
    $(document).ready(function() {
        $("#start").val(getURLParameter("start"));
        $("#end").val(getURLParameter("end"));
        $("#title").val(getURLParameter("title"));
        $("#content").val(getURLParameter("content"));
        $("#author").val(getURLParameter("author"));

    });
</script>

<table class="table">
    <thead>
        <tr>
            <th>Title</th>
            <th>Author</th>
            <th>Publish Time</th>
            <th>Operations</th>
        </tr>
    </thead>
    <tbody>
    <c:if test="${ number==0 }">
        <tr >
            <td colSpan=4 class="prompt">
                No post found, please search again with other conditions.
            </td>
        </tr>
    </c:if>
    <c:forEach var="post" items="${posts}" varStatus="row">
        <tr>
            <td>
                <a href="<c:url value='/posts/${post.postId}' />">
                    <c:out value="${post.title}"/>
                </a>
            </td>
               <td>
               <a href="<c:url value='/user/${users[row.index].id}' />">
               <c:out value="${post.authorName}"/></td>
            <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <td><fmt:formatDate value="${post.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/> </td>

                <c:choose>
                <c:when test="${not ifLike[row.index]}">
                    <td><a href="javascript:void(0)" onclick="like_confirm('${post.postId}');">Like</a></td>
                </c:when>
                <c:otherwise><td>Liked</td></c:otherwise>
                </c:choose>
        </tr>
    </c:forEach>
    </tbody>
</table>

<form action="search" method="get">
<table class="table">
    <thead>
        <th class="row" colSpan=2 >Please fill search condition</th>
    </thead>
    <tbody>
        <tr>
            <td>
                <label class="control-label">Title:</label>
                <div class="controls">
                    <input class="form-control" type='text' id="title" name='title'/>
                </div>
            </td>
            <td>
                <label class="control-label">Content:</label>
                <div class="controls">
                    <input class="form-control" type='text' id="content" name='content'/>
                </div>
            </td>
        </tr>
        <tr>
            <td>
                <label class="control-label">Publish Time From:</label>
                <div class="controls">
                    <input class="form-control" type='date'  id="start" name='start'/>
                </div>
            </td>
            <td>
                <label class="control-label">Publish Time To:</label>
                <div class="controls">
                    <input class="form-control" type='date' id="end" name='end'/>
                </div>
            </td>
        </tr>
        <tr>
            <td>
               <label class="control-label">Author:</label>
               <div class="controls">
                   <input class="form-control" type='text' id="author" name='author' />
               </div>
            </td>
            <td>
               <button type="submit" class="btn btn-primary">Search</button>
            </td>
        </tr>
    </tbody>
</table>
</form>

<script type="text/javascript">

function like_confirm(likedPostId)
{
    alert("you liked this post")
    document.LikePostForm.likePost.value = likedPostId;
    document.LikePostForm.submit();
}

</script>
<form name="LikePostForm" method="post" onsubmit="return form_validate();">
     <input type="hidden" id="likePost" name="likePost">
</form>

<%@ include file="footer.jsp" %>
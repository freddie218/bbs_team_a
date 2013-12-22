<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" scope="request" value="Home"/>

<%@ include file="header.jsp" %>

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

<form action="<c:url value='/user/create' />" method="post">
<table class="table">
    <thead>
        <th class="row" colSpan=2 >Please fill search condition</th>
    </thead>
    <tbody>
        <tr>
            <td>
                <label class="control-label">Title:</label>
                <div class="controls">
                    <input class="form-control" type='text' id="search_title" name='search_title'/>
                </div>
            </td>
            <td>
                <label class="control-label">Content:</label>
                <div class="controls">
                    <input class="form-control" type='text' id="search_content" name='search_content'/>
                </div>
            </td>
        </tr>
        <tr>
            <td>
                <label class="control-label">Publish Time From:</label>
                <div class="controls">
                    <input class="form-control" type='date'  id="search_start" name='search_start'/>
                </div>
            </td>
            <td>
                <label class="control-label">Publish Time To:</label>
                <div class="controls">
                    <input class="form-control" type='date' id="search_end" name='search_end'/>
                </div>
            </td>
        </tr>
        <tr>
            <td>
               <label class="control-label">Author:</label>
               <div class="controls">
                   <input class="form-control"" type='text' id="search_author" name='search_author'/>
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
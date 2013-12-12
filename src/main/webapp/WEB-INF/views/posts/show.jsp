<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" scope="request" value="Home"/>

<%@ include file="../header.jsp" %>




<table class="table">
    <thead>
        <tr>
            <th>Title</th>
            <th>Author</th>
            <th>Publish Time</th>
            <th>Content</th>
            <th>Liked</th>
            <th>Operations</th>
            <th></th>
        </tr>
    </thead>

    <tbody>
        <c:forEach var="post" items="${posts}" varStatus="row">
            <tr>
                <td><c:out value="${post.title}"/></td>
                <td><c:out value="${post.authorName}"/></td>
                <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
                <td><fmt:formatDate value="${post.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                <td><c:out value="${post.content}"/></td>
                <td>
                    <c:if test="${row.index==0}">
                        <c:out value="${post.liked_time} times"/>
                    </c:if>
                </td>
                <td>
                    <c:if test="${row.index==0}">
                        <c:choose>
                           <c:when test="${not like}">
                              <a href="javascript:void(0)" onclick="like_confirm('${post.postId}');">Like</a>
                           </c:when>
                           <c:otherwise><td>Liked</td></c:otherwise>
                        </c:choose>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
    </tbody>
</table>

<script type="text/javascript">
function like_confirm(likedPostId)
{
    alert("you liked this post")
    document.LikePostForm.likePost.value = likedPostId;
    document.LikePostForm.submit();
}

</script>
<form name="LikePostForm" method="post" action="likeProcess" onsubmit="return form_validate();">
     <input type="hidden" id="likePost" name="likePost">
</form>

</br>
     <c:choose>
        <c:when test="${not empty error}">
            <div id="replyPostError" class="page-action create-error">
                Content cannot be empty!
                ${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}
            </div>
        </c:when>
        <c:otherwise>
            <div id="createHint" class="page-action">

            </div>
        </c:otherwise>
    </c:choose>

<div id="createPanel">
    <form name="replyPost" id="replyPost" method="post">
        <input type="hidden" id="parentId" name="parentId" value="${mainPost.postId}" />
        <input type="hidden" id="title" name="title" value="Re: ${mainPost.title}" />
        <input type="hidden" id="likePost" name="likePost" />
        <textarea name="content" id="content"  placeholder="post content" cols="100" rows="6"></textarea>
        <button type="submit" class="btn">Create</button>
    </form>
</div>

</br>

<%@ include file="../footer.jsp" %>
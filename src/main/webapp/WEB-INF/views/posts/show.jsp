<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" scope="request" value="Home"/>

<%@ include file="../header.jsp" %>

<c:forEach var="post" items="${posts}" varStatus="row">
    <div class="form-for-post">
        <c:if test="${post.parentId eq 0}"><h1 style="text-align: center;">${post.title}</h1></c:if>
        <c:if test="${post.parentId > 0}"><h3 style="text-align: center;">${post.title}</h3></c:if>
        <p style="text-align: center;">Author:&nbsp;&nbsp;
         <a href="<c:url value='/user/${post.modifierId}' />">
         <strong>${post.authorName}</strong></a>
         &nbsp;&nbsp;&nbsp;&nbsp;Create
         Time:&nbsp;&nbsp;
        <strong><fmt:formatDate value="${post.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></strong>
        <c:if test="${post.parentId eq 0}">
         <span class="jiathis_style">
             <a class="jiathis_button_renren"></a>
             <a class="jiathis_button_tsina"></a>
             <a class="jiathis_button_douban"></a>
             <a class="jiathis_button_weixin"></a>
         </span>
         <script type="text/javascript" >
             var jiathis_config={
                sm:"renren,weixin",
                summary:"${post.content}",
                title:"${post.title}",
                shortUrl:false,
                hideMore:false
             }
         </script>
         <script type="text/javascript" src="http://v3.jiathis.com/code_mini/jia.js" charset="utf-8"></script>
        </c:if>
        </p>

        <p class="form-for-post-content">${post.content}</p>

        <p style="float:left; width:auto;">
             <c:if test="${post.parentId eq 0}">
                <span>Tags:</span>
                <c:forEach var="postTag" items="${tagLabels}" varStatus="row">
                    <c:if test="${not empty postTag}">
                        <span id="tagLabel">${postTag}</span>
                    </c:if>
                </c:forEach>
             </c:if>
        </p>


        <p style="position:absolute; margin: 0 0 0 250px">Operations:
            <strong>
                <c:if test="${post.parentId eq 0}">
                    <c:choose>
                        <c:when test="${not like}">
                            <a href="javascript:void(0)" onclick="like_confirm('${post.postId}');">Like</a>
                        </c:when>
                        <c:otherwise>Liked</c:otherwise>
                    </c:choose>
                </c:if>
                
                <c:if test="${not empty isMyMainPost}">
                    <a href="javascript:void(0);" onclick="deletePost('${post.postId}');">X</a>
                </c:if>
            </strong>
        </p>
    </div>
</c:forEach>


<script type="text/javascript">
function like_confirm(likedPostId)
{
    alert("you liked this post")
    document.LikePostForm.likePost.value = likedPostId;
    document.LikePostForm.submit();
}

</script>

<form name="LikePostForm" method="post" action="likeProcess" >
     <input type="hidden" id="likePost" name="likePost">
</form>

</br>
     <c:choose>
        <c:when test="${not empty error}">
            <div id="replyPostError" class="page-action create-error">
                Content cannot be empty!
            </div>
        </c:when>
        <c:otherwise>
             <c:if test="${not empty illegal}">
                 <div id="replyCreateHint" class="page-action create-error">${illegal}</div>
             </c:if>
        </c:otherwise>
    </c:choose>

<div id="createPanel">
    <form name="replyPost" id="replyPost" method="post" >
        <input type="hidden" id="parentId" name="parentId" value="${mainPost.postId}" />
        <input type="hidden" id="title" name="title" value="Re: ${mainPost.title}" />
        <input type="hidden" id="likePost" name="likePost" />
        <textarea name="content" id="content"  placeholder="post content" style="margin:0 10%;width: 80%;height: 100px;"></textarea>
        <button type="submit" class="btn" style="margin:10px 0 0 10%;">Create</button>
    </form>
</div>

</br>

<script type="text/javascript">
function deletePost(PostId)
{
    var isConfirmed = confirm("Are you sure to delete this post?");
    if(isConfirmed) {
        document.deletePostById.postIdToDel.value = PostId;
        document.deletePostById.submit();
    }
}

</script>
<form name="deletePostById" method="post" action='<c:url value="del/${postId}" />' >
     <input type="hidden" id="postIdToDel" name="postIdToDel" value="" >
</form>

<script type="text/javascript" src="<c:url value='/scripts/violations.js' />"></script>
<%@ include file="../footer.jsp" %>

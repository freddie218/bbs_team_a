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
    $(function(){
       $("#start").datepicker({
        defaultDate: "+1w",
        dateFormat: "yy-mm-dd",
        changeMonth: true,
        changeYear: true,
        onClose: function( selectedDate ) {
            $( "#end" ).datepicker( "option", "minDate", selectedDate );
        }
        });
       $("#end").datepicker({
        defaultDate: "+1w",
        dateFormat: "yy-mm-dd",
        changeMonth: true,
        changeYear: true,
        onClose: function( selectedDate ) {
            $( "#start" ).datepicker( "option", "maxDate", selectedDate );
        }
        });
    });

</script>

<table class="table table-hover">
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
            <td>
                <c:choose>
                <c:when test="${not ifLike[row.index]}">
                    <a href="javascript:void(0)" onclick="like_confirm('${post.postId}');">Like</a>
                </c:when>
                <c:otherwise>Liked</c:otherwise>
                </c:choose>
                <c:choose>
                     <c:when test="${post.top}">
                         <span> | Topped</span>
                     </c:when>
                     <c:otherwise>
                          <security:authorize ifAnyGranted="ROLE_ADMIN">
                              <span> | </span>
                              <a href="javascript:void(0)" onclick="topPost('${post.postId}');">Top</a>
                          </security:authorize>
                     </c:otherwise>
                </c:choose>
            </td>
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
                    <input class="form-control" type='text'  id="start" name='start'/>
                </div>
            </td>
            <td>
                <label class="control-label">Publish Time To:</label>
                <div class="controls">
                    <input class="form-control" type='text' id="end" name='end'/>
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

<script type="text/javascript">
function topPost(PostId)
{
    var isConfirmed = confirm("Are you sure to top this post?");
    if(isConfirmed) {
        document.topPostById.postIdToTop.value = PostId;
        document.topPostById.submit();
    }
}

</script>
<form name="topPostById" method="post" action='<c:url value="top/${postId}" />' >
     <input type="hidden" id="postIdToTop" name="postIdToTop" value="" >
</form>
<%@ include file="footer.jsp" %>
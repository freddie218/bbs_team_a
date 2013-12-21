<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" scope="request" value="Profile"/>

<%@ include file="../header.jsp" %>

<c:choose>
	<c:when test="${not empty error}">
		<div class="page-action">
			<div class="alert alert-danger col-sm-offset-2 col-sm-4">
				Failed to change password!
			</div>
		</div>
	</c:when>
	<c:when test="${not empty success}">
		<div class="page-action">
			<div class="alert alert-success col-sm-offset-2 col-sm-4">
				Password changed successfully.
			</div>
		</div>
	</c:when>
	<c:when test="${not empty namesuccess}">
        <div style="font-size: 16pt; margin-bottom: 10px; border-radius: 5px;">
            <div style="background-color:green; width:530px; text-align:center;">
             User Profile updated successfully
            </div>
        </div>
    </c:when>
</c:choose>

<div id="userInformation">
    <table class="table">
        <tr>
            <th class="first">Name</th>
            <c:if test="${empty showUser}">
                <td>${user.userName}</td>
            </c:if>
            <c:if test="${not empty showUser}">
                <td>${showUser.userName}</td>
            </c:if>
        </tr>
        <tr>
            <th class="first">Enable</th>
            <td>${user.enabled}</td>
        </tr>
    </table>

    <div class="table">

            <c:if test="${showUser.id==user.id}">
                <a href="changePassword">Change Password</a>
                <span id="line"></span>
                <a href="updateProfile">Update Profile</a>
            </c:if>

            <c:if test="${empty showUser}">
                <a href="changePassword">Change Password</a>
                <span id="line"></span>
                <a href="updateProfile">Update Profile</a>
            </c:if>


    </div>

</div>

<div id="myPost">
    <c:choose>
    <c:when test="${empty showUser}">
         <h2 size=24px>My Posts</h2>
    </c:when>
    <c:otherwise>
       <c:if test="${showUser.id!=user.id}">
          <h2>Posts</h2>
       </c:if>
       <c:if test="${showUser.id==user.id}">
          <h2>My Posts</h2>
       </c:if>
    </c:otherwise>
    </c:choose>

    <table class="table table-striped">
        <thead>
        <tr class="first">
            <th>Title</th>
            <th>Author</th>
            <th>Publish Time</th>
            <c:choose>
                <c:when test="${empty showUser}">
                     <th>Operations</th>
                </c:when>
                <c:otherwise>
                   <c:if test="${showUser.id==user.id}">
                       <th>Operations</th>
                   </c:if>
                    <c:if test="${showUser.id!=user.id}">

                    </c:if>
                </c:otherwise>
            </c:choose>

        </tr>
        </thead>
        <tbody>

        </script>

        <c:forEach var="post" items="${myPosts}" varStatus="row">
            <tr>
                <td>
                    <a href="<c:url value='/posts/${post.postId}' />">
                        <c:out value="${post.title}"/>
                    </a>
                </td>
                <td><c:out value="${post.authorName}"/></td>
                <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
                <td><fmt:formatDate value="${post.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>

                 <c:choose>
                    <c:when test="${empty showUser}">
                         <td><a href="javascript:void(0)" onclick="show_confirm('${post.postId}');">X</a></td>
                    </c:when>
                         <c:otherwise>
                            <c:if test="${showUser.id==user.id}">
                               <td><a href="javascript:void(0)" onclick="show_confirm('${post.postId}');">X</a></td>
                            </c:if>
                         </c:otherwise>
                 </c:choose>

            </tr>
        </c:forEach>
        </tbody>
    </table>

</div>

<script type="text/javascript">
function show_confirm(deletePostId)
{
var r=confirm("Are you sure to delete this post?");
if (r==true)
  {
      document.deletePostForm.deletePost.value = deletePostId;
      document.deletePostForm.submit();
  }
}
</script>
<form name="deletePostForm" method="post" onsubmit="return form_validate();">
     <input type="hidden" id="deletePost" name="deletePost">
</form>


<%@ include file="../footer.jsp" %>
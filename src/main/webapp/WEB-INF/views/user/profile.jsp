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
</c:choose>

<div id="userInformation">
    <table class="table">
        <tr>
            <td>Name</td>
            <td>${user.userName}</td>
        </tr>
        <tr>
            <td>Enable</td>
            <td>${user.enabled}</td>
        </tr>
    </table>
    <a href="changePassword">Change Password</a>
    <a href="updateProfile">Update Profile</a>
</div>

<div id="myPost">
    <h2> My Posts</h2>
    <table class="table table-striped">
        <thead>
        <tr>
            <th>Title</th>
            <th>Author</th>
            <th>Publish Time</th>
            <th></th>
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
            </tr>
        </c:forEach>
        </tbody>
    </table>

</div>

<%@ include file="../footer.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" scope="request" value="Profile"/>

<%@ include file="../header.jsp" %>

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
                <td><c:out value="${post.createTime}"/></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

</div>

<%@ include file="../footer.jsp" %>
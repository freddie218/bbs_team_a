<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" scope="request" value="Home"/>

<%@ include file="header.jsp" %>

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

    <c:forEach var="post" items="${posts}" varStatus="row">
        <tr>
            <td>
                <a href="<c:url value='/posts/${posts[row.index].postId}' />">
                    <c:out value="${posts[row.index].title}"/>
                </a>
            </td>
               <td>
               <a href="<c:url value='/user/${users[row.index].id}' />">
               <c:out value="${posts[row.index].authorName}"/></td>
            <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <td><fmt:formatDate value="${posts[row.index].createTime}" pattern="yyyy-MM-dd HH:mm:ss"/> </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<%@ include file="footer.jsp" %>
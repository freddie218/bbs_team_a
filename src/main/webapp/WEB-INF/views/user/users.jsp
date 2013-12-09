<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" scope="request" value="users"/>

<%@ include file="../header.jsp" %>

<div id="AllUsersInformation">
    <table class="table">
    <thead>
        <tr>
            <th>Username</th>
            <th>Enable</th>
            <th>Role</th>
            <th>Disable</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="map" items="${usersWithRoles}" varStatus="row">
            <tr>
                <td><c:out value="${map.key.userName}"/></td>
                <td><c:out value="${map.key.enabled}"/></td>
                <td>
                    <c:if test="${map.value=='ROLE_ADMIN'}">
                        Administrator
                    </c:if>
                    <c:if test="${map.value=='ROLE_REGULAR'}">
                        User
                        <a href="javascript:void(0)" onclick="show_confirm('${map.key.id}');"><span class="text-primary">&uarr;</span></a>
                    </c:if>
                </td>
                 <td>
                    <c:if test="${map.key.enabled && map.value!='ROLE_ADMIN'}">
                       <a href="javascript: void(0);" onclick='disableUser("${map.key.userName}");'>X</a>
                    </c:if>
                 </td>
            </tr>
        </c:forEach>
    </tbody>
    </table>
</div>

<script type="text/javascript">
    function show_confirm(authoriseUserId)
    {
        var r=confirm("Are you sure to authorise this user?");
        if (r==true)
          {
              document.authoriseUserForm.authoriseUserId.value = authoriseUserId;
              document.authoriseUserForm.submit();
          }
    }
    function disableUser(userName){
       if(confirm("Are you sure to disable this user?")){
         disableForm.userName.value = userName;
         document.disableForm.submit();
       }
    }
</script>
<form name="authoriseUserForm" method="post" onsubmit="return true;">
     <input type="hidden" id="authoriseUserId" name="authoriseUserId">
</form>
<form name="disableForm" id="disableForm" method="post" action="disableUser">
    <input type="hidden" id="userName" name="userName"/>
</form>
<%@ include file="../footer.jsp" %>
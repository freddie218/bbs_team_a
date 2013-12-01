<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" scope="request" value="updateProfile"/>

<%@ include file="../header.jsp" %>
<c:choose>
    <c:when test="${not empty error}">
        <div class="page-action">
            <div class="alert alert-danger col-sm-4">
                User Profile update failed
            </div>
        </div>
    </c:when>
</c:choose>
<div id="newNameInformation">
    <table class="table">
        <tr>
            <p class="lead">Update Profile for ${user.userName}</p>
        </tr>
        <tr>
            <form class="form-horizontal" method="post" name="updateProfile">
                <td style= "width:100px">
                    <label class="control-label" for="inputUsername" style = "margin:12px 0">Username:</label>
                </td>
                <td>
                    <div class="controls">
                        <input type="text " id="newUsername" name="newUsername" placeholder="Username" style="height:40px;width:250px">
                        </div>
                        <div class="control-group">
                            <div style="text-align:left" >
                            <button type="submit" class="btn btn-primary" >Change</button>
                            </div>
                        </div>
                    </div>
                </td>
            </form>
        </tr>
        <tr>
    </table>
</div>
<%@ include file="../footer.jsp" %>

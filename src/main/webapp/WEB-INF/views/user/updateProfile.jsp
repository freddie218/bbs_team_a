<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" scope="request" value="updateProfile"/>

<%@ include file="../header.jsp" %>
<table>
    <tr>
        <td>
            <c:choose>
                <c:when test="${not empty error}">
                    <div style="font-size: 16pt; margin-bottom: 10px; border-radius: 5px;">
                        <div style="background-color:red; width:530px; text-align:center;" >
                            User Profile update failed
                        </div>
                    </div>
                </c:when>
            </c:choose>
        </td>
    </tr>
    <tr>
        <td>
            <p class="lead">Update Profile for ${user.userName}</p>
        </td>
    </tr>
</table>
<div id="newNameInformation">
    <table class="table">
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
                </td>
            </form>
        </tr>
    </table>
</div>
<%@ include file="../footer.jsp" %>

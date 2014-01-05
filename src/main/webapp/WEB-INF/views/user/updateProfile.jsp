<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" scope="request" value="updateProfile"/>

<%@ include file="../header.jsp" %>

<c:if test="${not empty error}">
    <p class="prompt-error">User Profile update failed</p>
</c:if>
<p class="lead">Update Profile for ${user.userName}</p>

<form class="form-horizontal" method="post" name="updateProfile">
        <div class="control-group">
            <label class="control-label" for="inputUsername">Username</label>
            <div class="controls">
                <input class="form-control" type="text" id="newUsername" name="newUsername" placeholder="Username">
            </div>
        </div>
        <div class="control-group">
            <div class="controls">
                <button type="submit" class="btn btn-primary">Change</button>
            </div>
        </div>
</form>

<%@ include file="../footer.jsp" %>

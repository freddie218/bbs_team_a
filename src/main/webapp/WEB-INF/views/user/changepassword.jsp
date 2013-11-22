<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" scope="request" value="changepassword"/>

<%@ include file="../header.jsp" %>

<div id="passwordPanel">
    <form class="form-horizontal" action="<c:url value='/user/changepassword' />" method="post">
        <div class="control-group">
            <label class="control-label" for="olderpassword">Older password</label>
            <div class="controls">
                <input type="text" placeholder="older password" id="olderpassword" name="olderpassword" />
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="newpassword">New Password</label>
            <div class="controls">
                <input type="password" placeholder="new password" id="newpassword" name="newpassword" />
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="confirmpassword">Confirm Password</label>
            <div class="controls">
                <input type="password" placeholder="confirm password" id="confirmpassword" name="confirmpassword" />
            </div>
        </div>
        <div class="control-group">
            <div class="controls">
                <button type="submit" class="btn">Change Password</button>
            </div>
        </div>
    </form>
</div>

<%@ include file="../footer.jsp" %>
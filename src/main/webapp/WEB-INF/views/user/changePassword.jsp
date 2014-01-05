<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" scope="request" value="changePassword"/>

<%@ include file="../header.jsp" %>

<div id="changePassword">
<p class="lead">Change Password</p>
<form class="form-horizontal" method="post" name="changePassword" onsubmit="return form_validate();">
    <div class="form-group">
        <label for="password" class="col-sm-3 control-label">Old password</label>
         <div class="col-sm-4">
          <input class="col-sm-4 form-control" type="password" id="password" name="password" placeholder="old password">
         </div>
        <div class="col-sm-5"><span name="pswdInfo" id="pswdInfo" class="text-info">Input your old password here.</span></div>
    </div>
    <div class="form-group">
        <label for="newPassword" class="col-sm-3 control-label">New password</label>
        <div class="col-sm-4">
            <input class="form-control" type="password" id="newPassword" name="newPassword" placeholder="new password" maxlength="12">
        </div>
        <div class="col-sm-5"><span name="newPassInfo" id="newPassInfo" class="text-info">New password here.</span></div>
    </div>
    <div class="form-group">
        <label for="confirmPassword" class="col-sm-3 control-label">Confirm password</label>
        <div class="col-sm-4">
           <input class="form-control" type="password" id="confirmPassword" name="confirmPassword" placeholder="confirm new password" maxlength="12">
        </div>
        <div class="col-sm-5">
          <span name="confirmInfo" id="confirmInfo" class="text-info">confirm new password here.</span>
        </div>
    </div>
    <div class="form-group">
        <div class="col-sm-offset-3 col-sm-9">
            <button type="submit" class="btn btn-primary">Change</button>
        </div>
    </div>
</form>
</div>
<script type="text/javascript" src="<c:url value='/scripts/passwordChange.js' />"></script>

<%@ include file="../footer.jsp" %>

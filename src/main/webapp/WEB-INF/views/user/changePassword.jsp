<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" scope="request" value="changePassword"/>

<%@ include file="../header.jsp" %>
<style type="text/css">
    .lead {
        margin-left:55px;
    }
    #changePassword .form-horizontal{
        background-color:#DDDDDD;
        max-width:650px;
        margin: 0 auto;
        text-align:left;
        padding: 20px;
    }

    #changePassword .control-group{
        padding: 5px;
    }

    .control-label{
        width:140px;
        }
</style>


<div id="changePassword">
<p class="lead">Change Password</p>
    <form class="form-horizontal" method="post" name="changePassword" onsubmit="return form_validate();">
        <div class="control-group">
            <label class="control-label" for="inputPassword">Old password</label>
            <input type="password" id="password" name="password" placeholder="old password" ">
            <span name="pswdInfo" id="pswdInfo" class="text-info">Input your old password here.</span>
        </div>
        <div class="control-group">
            <label class="control-label" for="newPassword">New password</label>
            <input type="password" id="newPassword" name="newPassword" placeholder="new password" maxlength="12" />
            <span name="newPassInfo" id="newPassInfo" class="text-info">New password here.</span>
        </div>
        <div class="control-group">
            <label class="control-label" for="confirmPassword">Confirm password</label>
            <input type="password" id="confirmPassword" name="confirmPassword" placeholder="confirm new password" maxlength="12" />
            <span name="confirmInfo" id="confirmInfo" class="text-info">confirm new password here.</span>
        </div>
        <div class="control-group">
            <div class="controls">
                <button type="submit" class="btn" style="background-color:#CCCCCC" >Change</button>
            </div>
        </div>
    </form>
</div>
<script type="text/javascript" src="<c:url value='/scripts/passwordChange.js' />"></script>

<%@ include file="../footer.jsp" %>

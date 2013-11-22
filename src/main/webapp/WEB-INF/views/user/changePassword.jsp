<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" scope="request" value="changePassword"/>

<%@ include file="../header.jsp" %>

<p class="lead">Change Password</p>

<form class="form-horizontal" method="post" name="changePassword" onsubmit="return form_validate();">
    <div class="control-group">
        <label class="control-label" for="inputPassword">Old password</label>
        <div class="controls">
            <input type="password" id="password" name="password" placeholder="old password" ">
        </div>
        <span name="pswdInfo" id="pswdInfo" class="help-inline text-info">Input your old password here.</span>
    </div>
    <div class="control-group">
        <label class="control-label" for="newPassword">New password</label>
        <div class="controls">
            <input type="password" id="newPassword" name="newPassword" placeholder="new password" maxlength="12" />
        </div>
        <span name="newPassInfo" id="newPassInfo" class="help-inline text-info">New password here.</span>
    </div>
    <div class="control-group">
        <label class="control-label" for="confirmPassword">Confirm password</label>
        <div class="controls">
            <input type="password" id="confirmPassword" name="confirmPassword" placeholder="confirm new password" maxlength="12" />
        </div>
        <span name="confirmInfo" id="confirmInfo" class="help-inline text-info">confirm new password here.</span>
    </div>
    <div class="control-group">
        <div class="controls col-sm-offset-3">
            <button type="submit" class="btn" >Change</button>
        </div>
    </div>
</form>

<script type="text/javascript" >
	function form_validate(){
		var pass = changePassword.password.value;
		var new_pass = changePassword.newPassword.value;
		var cfm_pass = changePassword.confirmPassword.value;
		var pass_rule=/^(\d[_0-9]{5,11})$/;
		if (pass == ""){
		    document.getElementById("password").focus();
		    document.getElementById("pswdInfo").className="help-line text-danger";
		    document.getElementById("pswdInfo").innerHTML="Password should not be blank!";
		    return(false);
		    }
		    else {
		    document.getElementById("pswdInfo").className="help-line text-success";
		    document.getElementById("pswdInfo").innerHTML="&#8730;";
		    }
			if (!pass_rule.test(new_pass)){
		    document.getElementById("newPassword").focus();
		    document.getElementById("newPassInfo").className="help-line text-danger";
		    document.getElementById("newPassInfo").innerHTML=
		    "New password must be 6-12 characters(starting with a digit, and merely containing '_' and '0-9')!";
		    return(false);
		    }
		    else if(new_pass == pass) {
		    document.getElementById("newPassword").focus();
		    document.getElementById("newPassInfo").className="help-line text-warning";
		    document.getElementById("newPassInfo").innerHTML=
		    "New password identical to the old. Check it if you are not intend to have this happen.";
		    return(false);
		    }
		    else{
		    document.getElementById("newPassInfo").className="help-line text-success";
		    document.getElementById("newPassInfo").innerHTML="&#8730;";
		    }
		if ( new_pass != cfm_pass){
		    	document.getElementById("confirmPassword").focus();
		    	document.getElementById("confirmInfo").className="help-line text-danger";
		    	document.getElementById("confirmInfo").innerHTML="Please confirm identical to new password!"
                return(false);
		    }
		    else {
		    	document.getElementById("confirmInfo").className="help-line text-success";
				document.getElementById("confirmInfo").innerHTML="&#8730;";
		    }
		return(true);
	}

</script>

<%@ include file="../footer.jsp" %>
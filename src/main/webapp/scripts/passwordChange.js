function form_validate(){
		var pass = $(password).get(0).value;
		var new_pass = $(newPassword).get(0).value;
		var cfm_pass = $(confirmPassword).get(0).value;
		var pass_rule=/^([a-zA-Z0-9]\w{5,11})$/;
		if (pass == ""){
		    document.getElementById("password").focus();
		    document.getElementById("pswdInfo").className="text-danger";
		    document.getElementById("pswdInfo").innerHTML="Password should not be blank!";
		    return(false);
		    }
		    else {
		    document.getElementById("pswdInfo").className="text-success";
		    document.getElementById("pswdInfo").innerHTML="&#8730;";
		    }
			if (!pass_rule.test(new_pass)){
		    document.getElementById("newPassword").focus();
		    document.getElementById("newPassInfo").className="text-danger";
		    document.getElementById("newPassInfo").innerHTML=
		    "New password must be 6-12 characters(merely containing letters, '_'and '0-9' and not starting with a '_')!";
		    return(false);
		    }
		    else if(new_pass == pass) {
		    document.getElementById("newPassword").focus();
		    document.getElementById("newPassInfo").className="text-warning";
		    document.getElementById("newPassInfo").innerHTML=
		    "New password identical to the old. Check it if you are not intend to have this happen.";
		    return(false);
		    }
		    else{
		    document.getElementById("newPassInfo").className="text-success";
		    document.getElementById("newPassInfo").innerHTML="&#8730;";
		    }
		if ( new_pass != cfm_pass){
		    	document.getElementById("confirmPassword").focus();
		    	document.getElementById("confirmInfo").className="text-danger";
		    	document.getElementById("confirmInfo").innerHTML="Please confirm identical to new password!"
                return(false);
		    }
		    else {
		    	document.getElementById("confirmInfo").className="text-success";
				document.getElementById("confirmInfo").innerHTML="&#8730;";
		    }
		return(true);
}
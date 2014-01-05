<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" scope="request" value="Register"/>

<%@ include file="../header.jsp" %>
<script type="text/javascript">

function password_validate(){
    var password = $("#password").val();
    var regex = /^[a-zA-Z0-9]\w{5,11}$/;
    var element = $(".prompt-error").get(0);
    if(!regex.test(password)){
       $(element).html("Password required between 6 to 12 length of chars, numbers and underscore! ");
       return false;
    }else{
       $(element).html("");
       return true;
    }

}

</script>


<div id="registerPanel">
    <form class="form-horizontal" action="<c:url value='/user/create' />" method="post" onsubmit="return password_validate();">
        <div class="control-group">
            <label class="control-label" for="username">Username</label>
            <div class="controls">
                <input class="form-control" type="text" placeholder="user name" id="username" name="username" required="required"/>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="password">Password</label>
            <div class="controls">
                <input class="form-control" type="password" placeholder="password" id="password" name="password" required="required"/>
            </div>
        </div>
        <div class="control-group">
            <span class="prompt-error"></span>
        </div>
        <div class="control-group">
            <div class="controls">
                <button type="submit" class="btn btn-primary">Create</button>
            </div>
        </div>
    </form>
</div>

<%@ include file="../footer.jsp" %>
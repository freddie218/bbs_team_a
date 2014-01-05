<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" scope="request" value="Register"/>

<%@ include file="../header.jsp" %>

<div id="registerPanel">
    <form class="form-horizontal" action="<c:url value='/user/create' />" method="post">
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
            <div class="controls">
                <button type="submit" class="btn btn-primary">Create</button>
            </div>
        </div>
    </form>
</div>

<%@ include file="../footer.jsp" %>
function show_confirm(authoriseUserId)
    {
        var r=confirm("Are you sure to authorise this user?");
        if (r==true)
        {
        var form = document.createElement("form");
        form.setAttribute("method", "POST");

        var hiddenField = document.createElement("input");
        hiddenField.setAttribute("name", "authoriseUserId");
        hiddenField.setAttribute("value", authoriseUserId);
        hiddenField.setAttribute("type", "hidden");

        form.appendChild(hiddenField);
        document.body.appendChild(form);
        form.submit();
        }
}

function disableUser(userName){
    if(confirm("Are you sure to disable this user?")){
    disableForm.userName.value = userName;
    document.disableForm.submit();
    }
}
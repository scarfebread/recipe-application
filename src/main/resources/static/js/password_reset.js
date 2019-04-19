function passwordReset()
{
    hideElement('invalidUsernameOrEmail');

    let emailOrUsername = getValueById('usernameoremail');

    let user = {
        username: null,
        password: null,
        email: null
    };

    if (validateEmail(emailOrUsername))
    {
        user.email = emailOrUsername;
    }
    else if (validateStringLength(emailOrUsername, 3))
    {
        user.username = emailOrUsername;
    }
    else
    {
        showElement('invalidUsernameOrEmail');
        return false;
    }

    fetch ("http://localhost:8080/api/password_reset", {
        method: 'POST',
        headers: {
            "Content-Type": "application/json"
        },
        credentials: "same-origin",
        body: JSON.stringify(user)
    }).then(
        function (response) {
            hideElement('passwordResetForm');
            showElement('postPasswordResetDisplay');
        }
    );
}

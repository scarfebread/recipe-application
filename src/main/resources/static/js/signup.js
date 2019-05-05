function signup()
{
    resetErrors();

    let username = getValueById('username');
    let password = getValueById('password');
    let repeatPassword = getValueById('retypepassword');
    let email = getValueById('email');

    let isValid = true;

    if (!validatePasswordMatches(password, repeatPassword))
    {
        showElement('passwordsDoNotMatchError');
        isValid = false;
    }

    if (!validateStringLength(username, 3))
    {
        showElement('usernameTooShortError');
        isValid = false;
    }

    if (!validateStringLength(password, 5))
    {
        showElement('passwordTooShortError');
        isValid = false;
    }

    if (!validateEmail(email))
    {
        showElement('emailNotValid');
        isValid = false;
    }

    if (!isValid)
    {
        return false;
    }

    let user = {
        username: username,
        password: password,
        email: email
    };

    fetch ("/api/signup", {
        method: 'POST',
        headers: {
            "Content-Type": "application/json"
        },
        credentials: "same-origin",
        body: JSON.stringify(user)
    }).then(
        function (response) {
            if (response.status !== 201)
            {
                response.text().then(function(data) {
                    document.getElementById("signupError").innerText = data;
                    showElement('signupError');
                });

                return false;
            }

            hideElement('preSignupDisplay');
            showElement('postSignupDisplay');
            return true;
        }
    ).catch(
        function (error) {
            document.getElementById("signupError").value = error;
            showElement('signupError');
        }
    );

    return false;
}

function resetErrors()
{
    hideElement("passwordsDoNotMatchError");
    hideElement("emailNotValid");
    hideElement("signupError");
    hideElement('usernameTooShortError');
    hideElement('passwordTooShortError');
}

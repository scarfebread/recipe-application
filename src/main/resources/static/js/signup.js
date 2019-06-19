let signupEnabled = true;

document.addEventListener("DOMContentLoaded", function(event)
{
    let signupButton = getElementById('signupButton');

    signupButton.onclick = function ()
    {
        if (!signupEnabled) {
            return;
        }

        signupEnabled = false;

        signup();
    }
});

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

    let success = function() {
        hideElement('preSignupDisplay');
        showElement('postSignupDisplay');
    };

    let failure = function(failure) {
        document.getElementById("signupError").innerText = failure;
        showElement('signupError');

        signupEnabled = true;
    };

    callApi("/api/signup", HTTP_POST, user, false, success, failure);
}

function resetErrors()
{
    hideElement("passwordsDoNotMatchError");
    hideElement("emailNotValid");
    hideElement("signupError");
    hideElement('usernameTooShortError');
    hideElement('passwordTooShortError');
}

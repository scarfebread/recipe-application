document.addEventListener("DOMContentLoaded", function(event)
{
    let changePasswordButton = getElementById('changePasswordButton');

    changePasswordButton.onclick = function () {
        changePassword();
    }
});

function changePassword()
{
    resetErrors();

    console.log(document.getElementById('preChangePasswordDisplay'));

    let password = getValueById('password');
    let repeatPassword = getValueById('retypepassword');

    let isValid = true;

    if (!validatePasswordMatches(password, repeatPassword)) {
        showElement('passwordsDoNotMatchError');
        isValid = false;
    }

    if (!validateStringLength(password, 5)) {
        showElement('passwordTooShortError');
        isValid = false;
    }

    if (!isValid) {
        return false;
    }

    let passwordDto = {
        password: password
    };

    let success = function() {
        hideElement('preChangePasswordDisplay');
        showElement('postChangePasswordDisplay');
    };

    let failure = function(failure) {
        getElementById("passwordChangeError").innerText = failure;
        showElement('passwordChangeError');
    };

    callApi("/api/change_password", HTTP_POST, passwordDto, false, success, failure);
}

function resetErrors()
{
    hideElement("passwordsDoNotMatchError");
    hideElement("passwordChangeError");
    hideElement('passwordTooShortError');
}

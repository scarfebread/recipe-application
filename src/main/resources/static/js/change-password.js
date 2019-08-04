document.addEventListener("DOMContentLoaded", function(event)
{
    let changePasswordButton = getElementById('changePasswordButton');

    changePasswordButton.onclick = function () {
        changePassword();
    };

    addEnterKeyEventListener(getElementById('password'));
    addEnterKeyEventListener(getElementById('retypepassword'));
});

function changePassword()
{
    resetErrors();

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

    callApi("/api/change-password", HTTP_POST, passwordDto, false, success, failure);
}

function resetErrors()
{
    hideElement("passwordsDoNotMatchError");
    hideElement("passwordChangeError");
    hideElement('passwordTooShortError');
}

function addEnterKeyEventListener(element)
{
    element.addEventListener("keyup", function (event) {
        if (event.key === "Enter") {
            changePassword();
        }
    });
}

document.addEventListener("DOMContentLoaded", function() {
    ChangePassword.init();
});

ChangePassword = (function () {
    let addEventListeners = function () {
        let changePasswordButton = getElementById('changePasswordButton');

        changePasswordButton.onclick = function () {
            changePassword();
        };

        addEventListener(getElementById('password'));
        addEventListener(getElementById('retypepassword'));
    };

    let changePassword = function () {
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
    };

    let resetErrors = function () {
        hideElement("passwordsDoNotMatchError");
        hideElement("passwordChangeError");
        hideElement('passwordTooShortError');
    };

    let addEventListener = function (element) {
        element.addEventListener("keyup", function (event) {
            if (event.key === "Enter") {
                changePassword();
            }
        });
    };

    return {
        init: function () {
            addEventListeners();
        }
    }
})();

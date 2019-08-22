document.addEventListener("DOMContentLoaded", function() {
    ChangePassword.init();
});

export const ChangePassword = (function () {
    const addEventListeners = function () {
        const changePasswordButton = getElementById('changePasswordButton');

        changePasswordButton.onclick = function () {
            changePassword();
        };

        addEventListener(getElementById('password'));
        addEventListener(getElementById('retypepassword'));
    };

    const changePassword = function () {
        resetErrors();

        const password = getValueById('password');
        const repeatPassword = getValueById('retypepassword');

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

        const passwordDto = {
            password: password
        };

        const success = function() {
            hideElement('preChangePasswordDisplay');
            showElement('postChangePasswordDisplay');
        };

        const failure = function(failure) {
            getElementById("passwordChangeError").innerText = failure;
            showElement('passwordChangeError');
        };

        callApi("/api/change-password", HTTP_POST, passwordDto, false, success, failure);
    };

    const resetErrors = function () {
        hideElement("passwordsDoNotMatchError");
        hideElement("passwordChangeError");
        hideElement('passwordTooShortError');
    };

    const addEventListener = function (element) {
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

document.addEventListener("DOMContentLoaded", function() {
    ChangePassword.init();
});

ChangePassword = {
    init: function () {
        this.bindUserActions();
    },

    bindUserActions: function () {
        let changePasswordButton = getElementById('changePasswordButton');

        changePasswordButton.onclick = function () {
            ChangePassword.changePassword();
        };

        this.addEventListener(getElementById('password'));
        this.addEventListener(getElementById('retypepassword'));
    },

    changePassword: function () {
        this.resetErrors();

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
    },

    resetErrors: function () {
        hideElement("passwordsDoNotMatchError");
        hideElement("passwordChangeError");
        hideElement('passwordTooShortError');
    },

    addEventListener: function (element) {
        element.addEventListener("keyup", function (event) {
            if (event.key === "Enter") {
                ChangePassword.changePassword();
            }
        });
    },
};

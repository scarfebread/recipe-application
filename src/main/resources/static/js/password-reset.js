document.addEventListener("DOMContentLoaded", function() {
    PasswordReset.init();
});

PasswordReset = (function () {
    let addEventListeners = function () {
        let submitButton = getElementById('resetPasswordButton');
        submitButton.onclick = function () {
            reset();
        };

        let input = getElementById('usernameoremail');
        input.addEventListener("keydown", function (event) {
            if (event.key === "Enter") {
                reset();
            }
        });
    };

    let reset = function () {
        hideElement('invalidUsernameOrEmail');

        let emailOrUsername = getValueById('usernameoremail');

        let user = {
            username: null,
            password: null,
            email: null
        };

        if (validateEmail(emailOrUsername)) {
            user.email = emailOrUsername;
        } else if (validateStringLength(emailOrUsername, 3)) {
            user.username = emailOrUsername;
        } else {
            showElement('invalidUsernameOrEmail');
            return false;
        }

        let success = function() {
            hideElement('passwordResetForm');
            showElement('postPasswordResetDisplay');
        };

        let failure = function() {
            hideElement('passwordResetForm');
            showElement('postPasswordResetDisplay');
        };

        callApi("/api/password-reset", HTTP_POST, user, false, success, failure);
    };

    return {
        init: function () {
            addEventListeners();
        }
    }
})();

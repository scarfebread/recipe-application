document.addEventListener("DOMContentLoaded", function() {
    PasswordReset.init();
});

export const PasswordReset = (function () {
    const addEventListeners = function () {
        const submitButton = getElementById('resetPasswordButton');
        submitButton.onclick = function () {
            reset();
        };

        const input = getElementById('usernameoremail');
        input.addEventListener("keydown", function (event) {
            if (event.key === "Enter") {
                reset();
            }
        });
    };

    const reset = function () {
        hideElement('invalidUsernameOrEmail');

        const emailOrUsername = getValueById('usernameoremail');

        const user = {
            username: null,
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

        const success = function() {
            hideElement('passwordResetForm');
            showElement('postPasswordResetDisplay');
        };

        const failure = function() {
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

document.addEventListener("DOMContentLoaded", function() {
    Signup.init()
});

export const Signup = (function () {
    let enabled = true;
        
    const addEventListeners = function () {
        const signupButton = getElementById('signupButton');

        addEnterKeyListener(getElementById('username'));
        addEnterKeyListener(getElementById('password'));
        addEnterKeyListener(getElementById('retypepassword'));
        addEnterKeyListener(getElementById('email'));

        signupButton.onclick = function () {
            signup();
        };
    };

    const signup = function () {
        if (!enabled) {
            return;
        }

        enabled = false;

        resetErrors();

        const username = getValueById('username');
        const password = getValueById('password');
        const repeatPassword = getValueById('retypepassword');
        const email = getValueById('email');

        let isValid = true;

        if (!validatePasswordMatches(password, repeatPassword)) {
            showElement('passwordsDoNotMatchError');
            isValid = false;
        }

        if (!validateStringLength(username, 3)) {
            showElement('usernameTooShortError');
            isValid = false;
        }

        if (!validateStringLength(password, 5)) {
            showElement('passwordTooShortError');
            isValid = false;
        }

        if (!validateEmail(email)) {
            showElement('emailNotValid');
            isValid = false;
        }

        if (!isValid) {
            enabled = true;
            return false;
        }

        const user = {
            username: username,
            password: password,
            email: email
        };

        const success = function() {
            hideElement('preSignupDisplay');
            showElement('postSignupDisplay');
        };

        const failure = function(failure) {
            document.getElementById("signupError").innerText = failure;
            showElement('signupError');

            enabled = true;
        };

        callApi("/api/signup", HTTP_POST, user, false, success, failure);
    };

    const resetErrors = function () {
        hideElement("passwordsDoNotMatchError");
        hideElement("emailNotValid");
        hideElement("signupError");
        hideElement('usernameTooShortError');
        hideElement('passwordTooShortError');
    };

    const addEnterKeyListener = function (element) {
        element.addEventListener("keydown", function (event) {
            if (event.key === "Enter") {
                signup();
            }
        });
    };

    return {
        init: function () {
            addEventListeners();
        }
    }
})();

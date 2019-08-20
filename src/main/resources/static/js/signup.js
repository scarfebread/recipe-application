document.addEventListener("DOMContentLoaded", function() {
    Signup.init()
});

Signup = (function () {
    let enabled = true;
        
    let addEventListeners = function () {
        let signupButton = getElementById('signupButton');

        addEnterKeyListener(getElementById('username'));
        addEnterKeyListener(getElementById('password'));
        addEnterKeyListener(getElementById('retypepassword'));
        addEnterKeyListener(getElementById('email'));

        signupButton.onclick = function () {
            signup();
        };
    };

    let signup = function () {
        if (!enabled) {
            return;
        }

        enabled = false;

        resetErrors();

        let username = getValueById('username');
        let password = getValueById('password');
        let repeatPassword = getValueById('retypepassword');
        let email = getValueById('email');

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

            enabled = true;
        };

        callApi("/api/signup", HTTP_POST, user, false, success, failure);
    };

    let resetErrors = function () {
        hideElement("passwordsDoNotMatchError");
        hideElement("emailNotValid");
        hideElement("signupError");
        hideElement('usernameTooShortError');
        hideElement('passwordTooShortError');
    };

    let addEnterKeyListener = function (element) {
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

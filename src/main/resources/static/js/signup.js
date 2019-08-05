document.addEventListener("DOMContentLoaded", function(event) {
    Signup.init()
});

Signup = {
    enabled: true,

    init: function () {
        this.bindUserActions();
    },

    bindUserActions: function () {
        let signupButton = getElementById('signupButton');

        this.addEnterKeyListener(getElementById('username'));
        this.addEnterKeyListener(getElementById('password'));
        this.addEnterKeyListener(getElementById('retypepassword'));
        this.addEnterKeyListener(getElementById('email'));

        signupButton.onclick = function () {
            Signup.signup();
        };
    },

    signup: function () {
        if (!this.enabled) {
            return;
        }

        this.enabled = false;

        this.resetErrors();

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
            this.enabled = true;
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

            Signup.enabled = true;
        };

        callApi("/api/signup", HTTP_POST, user, false, success, failure);
    },

    resetErrors: function () {
        hideElement("passwordsDoNotMatchError");
        hideElement("emailNotValid");
        hideElement("signupError");
        hideElement('usernameTooShortError');
        hideElement('passwordTooShortError');
    },

    addEnterKeyListener: function (element) {
        element.addEventListener("keydown", function (event) {
            if (event.key === "Enter") {
                Signup.signup();
            }
        });
    }
};

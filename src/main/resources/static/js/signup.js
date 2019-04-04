function signup()
{
    resetErrors();

    let username = document.getElementById('username').value;
    let password = document.getElementById('password').value;
    let repeatPassword = document.getElementById('retypepassword').value;
    let email = document.getElementById('email').value.toLowerCase();

    let isValid = true;

    if (!validatePasswordMatches(password, repeatPassword))
    {
        showElement('passwordsDoNotMatchError');
        isValid = false;
    }

    if (!validateStringLength(username, 3))
    {
        showElement('usernameTooShortError');
        isValid = false;
    }

    if (!validateStringLength(password, 5))
    {
        showElement('passwordTooShortError');
        isValid = false;
    }

    if (!validateEmail(email))
    {
        showElement('emailNotValid');
        isValid = false;
    }

    if (!isValid)
    {
        return false;
    }

    let user = {
        username: username,
        password: password,
        email: email
    };

    fetch ("http://localhost:8080/api/signup", {
        method: 'POST',
        headers: {
            "Content-Type": "application/json"
        },
        credentials: "same-origin",
        body: JSON.stringify(user)
    }).then(
        function (response) {
            if (response.status !== 201)
            {
                response.text().then(function(data) {
                    console.log(data);
                    document.getElementById("signupError").innerText = data;
                    showElement('signupError');
                });

                return false;
            }

            hideElement('preSignupDisplay');
            showElement('postSignupDisplay');
            return true;
        }
    ).catch(
        function (error) {
            document.getElementById("signupError").value = error;
            showElement('signupError');
        }
    );

    return false;
}

function validatePasswordMatches(password, repeatPassword)
{
    return password === repeatPassword;
}

function validateStringLength(string, length)
{
    return string.length >= length;
}

function validateEmail(email)
{
    let validateEmailRegularExpression = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return validateEmailRegularExpression.test(email);
}

function resetErrors()
{
    hideElement("passwordsDoNotMatchError");
    hideElement("emailNotValid");
    hideElement("signupError");
    hideElement('usernameTooShortError');
    hideElement('passwordTooShortError');
}

function showElement(id)
{
    document.getElementById(id).style.display = "block";
}

function hideElement(id)
{
    document.getElementById(id).style.display = "none";
}
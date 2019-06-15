document.addEventListener("DOMContentLoaded", function(event)
{
    let changePasswordButton = getElementById('changePasswordButton');

    changePasswordButton.onclick = function () {
        changePassword();
    }
});

function changePassword()
{
    resetErrors();

    console.log(document.getElementById('preChangePasswordDisplay'));

    let password = getValueById('password');
    let repeatPassword = getValueById('retypepassword');

    let isValid = true;

    if (!validatePasswordMatches(password, repeatPassword))
    {
        showElement('passwordsDoNotMatchError');
        isValid = false;
    }

    if (!validateStringLength(password, 5))
    {
        showElement('passwordTooShortError');
        isValid = false;
    }

    if (!isValid)
    {
        return false;
    }

    let passwordDto = {
        password: password
    };

    fetch ("/api/change_password", {
        method: 'POST',
        headers: {
            "Content-Type": "application/json"
        },
        credentials: "same-origin",
        body: JSON.stringify(passwordDto)
    }).then(
        function (response) {
            if (response.status !== 201)
            {
                response.text().then(function(data) {
                    document.getElementById("passwordChangeError").innerText = data;
                    showElement('passwordChangeError');
                });

                return false;
            }

            hideElement('preChangePasswordDisplay');
            showElement('postChangePasswordDisplay');
            return true;
        }
    ).catch(
        function (error) {
            document.getElementById("passwordChangeError").value = error;
            showElement('passwordChangeError');
        }
    );

    return false;
}

function resetErrors()
{
    hideElement("passwordsDoNotMatchError");
    hideElement("passwordChangeError");
    hideElement('passwordTooShortError');
}

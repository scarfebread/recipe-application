// Constants
const HTTP_GET = 'GET';
const HTTP_POST = 'POST';
const HTTP_DELETE = 'DELETE';
const HTTP_PUT = 'PUT';

function showElement(id)
{
    document.getElementById(id).style.display = "block";
}

function hideElement(id)
{
    document.getElementById(id).style.display = "none";
}

function validateEmail(email)
{
    let validateEmailRegularExpression = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return validateEmailRegularExpression.test(email);
}

function validateStringLength(string, length)
{
    return string.length >= length;
}

function validatePasswordMatches(password, repeatPassword)
{
    return password === repeatPassword;
}

function getElementById(id)
{
    return document.getElementById(id);
}

function getValueById(id)
{
    return getElementById(id).value;
}

function createElement(tag)
{
    return document.createElement(tag);
}

function removeChildElements(element)
{
    while (element.firstChild)
    {
        element.removeChild(element.firstChild);
    }
}

function callApi(url, method, body, jsonResponse, success, failure)
{
    fetch (url, {
        method: method,
        headers: {
            "Content-Type": "application/json"
        },
        credentials: "same-origin",
        body: JSON.stringify(body)
    }).then(
        function (response) {
            if (!response.ok)
            {
                response.text().then(function(data) {
                    failure(data);
                });

                return;
            }

            if (jsonResponse) {
                response.json().then(function(data) {
                    success(data);
                });
            } else {
                success()
            }
        }
    ).catch(
        function (error) {
            failure(error);
        }
    );
}

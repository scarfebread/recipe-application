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

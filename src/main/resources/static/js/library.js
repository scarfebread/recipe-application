const HTTP_GET = 'GET';
const HTTP_POST = 'POST';
const HTTP_DELETE = 'DELETE';
const HTTP_PUT = 'PUT';

function showElement(id) {
    document.getElementById(id).style.display = "block";
}

function hideElement(id) {
    document.getElementById(id).style.display = "none";
}

function validateEmail(email) {
    const validateEmailRegularExpression = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return validateEmailRegularExpression.test(email);
}

function validateStringLength(string, length) {
    return string.length >= length;
}

function validatePasswordMatches(password, repeatPassword) {
    return password === repeatPassword;
}

function getElementById(id) {
    return document.getElementById(id);
}

function getValueById(id) {
    return getElementById(id).value;
}

function callApi(url, method, body, jsonResponse, success, failure) {
    const request = {
        method: method,
        headers: {
            "Content-Type": "application/json"
        },
        credentials: "same-origin"
    };

    if (body) {
        request.body = JSON.stringify(body);
    }

    fetch (url, request).then(
        function (response) {
            if (!response.ok) {
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

function getTemplate(id) {
    return getElementById(id).content.cloneNode(true);
}

function autocomplete(input, array) {
    let currentFocus;

    input.addEventListener("input", function(e) {
        let a, b, i, val = this.value;
        closeAllLists();

        if (!val) {
            return false;
        }
        currentFocus = -1;

        a = document.createElement("div");
        a.setAttribute("id", this.id + "autocomplete-list");
        a.setAttribute("class", "autocomplete-items");

        this.parentNode.appendChild(a);
        for (i = 0; i < array.length; i++) {
            if (array[i].substr(0, val.length).toUpperCase() === val.toUpperCase()) {
                b = document.createElement("div");
                b.innerHTML = "<strong>" + array[i].substr(0, val.length) + "</strong>";
                b.innerHTML += array[i].substr(val.length);
                b.innerHTML += "<input type='hidden' value='" + array[i] + "'>";
                b.addEventListener("click", function(e) {
                    input.value = this.getElementsByTagName("input")[0].value;
                    closeAllLists();
                });
                a.appendChild(b);
            }
        }
    });
    input.addEventListener("keydown", function(e) {
        let x = document.getElementById(this.id + "autocomplete-list");
        if (x) x = x.getElementsByTagName("div");
        if (e.keyCode === 40) {
            currentFocus++;
            addActive(x);
        } else if (e.keyCode === 38) {
            currentFocus--;
            addActive(x);
        } else if (e.keyCode === 13) {
            e.preventDefault();
            if (currentFocus > -1) {
                if (x) x[currentFocus].click();
            }
        }
    });
    function addActive(x) {
        if (!x) return false;
        removeActive(x);
        if (currentFocus >= x.length) currentFocus = 0;
        if (currentFocus < 0) currentFocus = (x.length - 1);
        x[currentFocus].classList.add("autocomplete-active");
    }
    function removeActive(x) {
        for (let i = 0; i < x.length; i++) {
            x[i].classList.remove("autocomplete-active");
        }
    }
    function closeAllLists(elmnt) {
        const x = document.getElementsByClassName("autocomplete-items");
        for (let i = 0; i < x.length; i++) {
            if (elmnt !== x[i] && elmnt !== input) {
                x[i].parentNode.removeChild(x[i]);
            }
        }
    }

    document.addEventListener("click", function (e) {
        closeAllLists(e.target);
    });
}

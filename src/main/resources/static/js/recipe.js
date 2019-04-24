document.addEventListener("DOMContentLoaded", function(event)
{
    let modal = document.getElementById('myModal');
    let createRecipeButton = document.getElementById("createRecipeButton");
    let closeModalButton = document.getElementsByClassName("close")[0];
    let confirmRecipeButton = getElementById('confirmRecipeButton');

    createRecipeButton.onclick = function()
    {
        hideElement('invalidRecipeNameError');
        modal.style.display = "block";
    };

    closeModalButton.onclick = function()
    {
        modal.style.display = "none";
    };

    window.onclick = function(event)
    {
        if (event.target === modal)
        {
            modal.style.display = "none";
        }
    };

    window.onkeydown = function(event)
    {
        if (event.key === 'Escape')
        {
            modal.style.display = "none";
        }
    };

    confirmRecipeButton.onclick = function ()
    {
        hideElement('invalidRecipeNameError');

        let recipe = {
            title: getValueById('recipeName')
        };

        if (!validateStringLength(recipe.title, 1))
        {
            showElement('invalidRecipeNameError');
            return false;
        }

        fetch ("http://localhost:8080/api/recipe", {
            method: 'POST',
            headers: {
                "Content-Type": "application/json"
            },
            credentials: "same-origin",
            body: JSON.stringify(recipe)
        }).then(
            function (response) {
                if (response.status !== 201)
                {
                    response.text().then(function(data) {
                        getElementById('createRecipeError').innerText = data;
                        showElement('createRecipeError');
                    });

                    return false;
                }

                hideElement('preRecipeCreated');
                showElement('postRecipeCreated');
                return true;
            }
        ).catch(
            function (error) {
                getElementById("createRecipeError").value = error;
                showElement('createRecipeError');
            }
        );
    };

    function getRecipes()
    {
        let recipes;

        fetch ("http://localhost:8080/api/recipe", {
            method: 'GET',
            headers: {
                "Content-Type": "application/json"
            },
            credentials: "same-origin",
        }).then(
            function (response) {
                if (response.status !== 200)
                {
                    response.text().then(function(data) {
                        // show error banner
                    });

                    return false;
                }


            }
        ).catch(
            function (error) {
                // show error banner
            }
        );
    }
});
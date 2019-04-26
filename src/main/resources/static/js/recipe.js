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
});

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

            // show recipes
        }
    ).catch(
        function (error) {
            // show error banner
        }
    );
}

function displayRecipe(recipe)
{
    let title = document.createElement('h3');
    title.innerText = recipe.title;
    title.className = 'recipeTitle';

    let link = document.createElement('a');
    link.href = `/recipe?id=${recipe.id}`;
    link.appendChild(title);

    let image = document.createElement('img');
    image.className = 'recipeThumbnail';
    image.src = recipe.image;

    let cookTime = document.createElement('label');
    cookTime.innerText = `Total time: ${recipe.totalCookTime}`;

    let listItem = document.createElement('div');
    listItem.className = 'recipeListItem';
    listItem.appendChild(link);
    listItem.appendChild(image);
    listItem.appendChild(cookTime);
    listItem.appendChild(document.createElement('br'));

    for (let i = 0; i < recipe.rating; i++)
    {
        let star = document.createElement('span');
        star.className = 'fa fa-star star checked';
        listItem.appendChild(star);
    }

    for (let i = 0; i < (5 - recipe.rating); i++)
    {
        let star = document.createElement('span');
        star.className = 'fa fa-star star';
        listItem.appendChild(star);
    }

    let list = getElementById('allRecipesContainer');
    list.appendChild(listItem);
}

function updateNavBar(recipes)
{
    let recipeDropdown = getElementById('recipeDropdown');

    if (recipes.length === 0)
    {
        let noRecipesLabel = createElement('label');
        noRecipesLabel.innerText = 'No recipes to display';
        recipeDropdown.appendChild(noRecipesLabel);
        return;
    }

    for (let recipe in recipes)
    {
        let recipeLink = createElement('a');
        recipeLink.href = `/recipe?id=${recipe.id}`;
        recipeLink.innerText = recipe.title;
        recipeDropdown.appendChild(recipeLink);
    }
}
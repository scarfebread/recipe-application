document.addEventListener("DOMContentLoaded", function(event)
{
    let modal = document.getElementById('myModal');
    let createRecipeButton = document.getElementById("createRecipeButton");
    let closeModalButton = document.getElementsByClassName("close")[0];
    let confirmRecipeButton = getElementById('confirmRecipeButton');

    createRecipeButton.onclick = function()
    {
        modal.style.display = "block";
    };

    closeModalButton.onclick = function()
    {
        closeModal(modal);
    };

    window.onclick = function(event)
    {
        if (event.target === modal)
        {
            closeModal(modal);
        }
    };

    window.onkeydown = function(event)
    {
        if (event.key === 'Escape')
        {
            closeModal(modal);
        }
    };

    confirmRecipeButton.onclick = function ()
    {
        confirmRecipeButton.disabled = true;

        hideElement('invalidRecipeNameError');

        let recipe = {
            title: getValueById('recipeName')
        };

        if (!validateStringLength(recipe.title, 1))
        {
            confirmRecipeButton.disabled = false;
            showElement('invalidRecipeNameError');
            return;
        }

        createRecipe(recipe);

        confirmRecipeButton.disabled = false;
    };

    getRecipes();
});

function getRecipes()
{
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
                    // TODO throw error
                });

                return false;
            }

            response.json().then(function(recipes) {
                updateNavBar(recipes);
                displayRecipes(recipes);

                enableAutocomplete(recipes);
            });

            return false;
        }
    ).catch(
        function (error) {
            // TODO show error banner
        }
    );
}

function displayRecipes(recipes)
{
    let allRecipes = getElementById('allRecipes');

    while (allRecipes.firstChild)
    {
        allRecipes.removeChild(allRecipes.firstChild);
    }

    for (let i in recipes)
    {
        displayRecipe(recipes[i]);
    }
}

function displayRecipe(recipe)
{
    let listItem = createElement('div');
    listItem.className = 'recipeListItem';

    let title = createElement('h3');
    title.innerText = recipe.title;
    title.className = 'recipeTitle';

    let link = createElement('a');
    link.href = `/recipe?id=${recipe.id}`;
    link.appendChild(title);
    listItem.appendChild(link);

    if (recipe.image != null)
    {
        let image = createElement('img');
        image.className = 'recipeThumbnail';
        image.src = recipe.image;
        listItem.appendChild(image);
    }

    // TODO properly set the cook time
    let cookTime = createElement('label');
    if (recipe.totalCookTime != null)
    {
        cookTime.innerText = `Total time: ${recipe.totalCookTime}`;
    }
    else
    {
        cookTime.innerText = 'Please set the cook time'
    }
    listItem.appendChild(cookTime);

    listItem.appendChild(createElement('br'));

    for (let i = 0; i < recipe.rating; i++)
    {
        let star = createElement('span');
        star.className = 'fa fa-star star checked';
        listItem.appendChild(star);
    }

    for (let i = 0; i < (5 - recipe.rating); i++)
    {
        let star = createElement('span');
        star.className = 'fa fa-star star';
        listItem.appendChild(star);
    }

    let list = getElementById('allRecipes');
    list.appendChild(listItem);
}

function updateNavBar(recipes)
{
    // TODO limit this to recent recipes / highest rated

    let recipeDropdown = getElementById('recipeDropdown');

    while (recipeDropdown.firstChild)
    {
        recipeDropdown.removeChild(recipeDropdown.firstChild);
    }

    if (recipes.length === 0)
    {
        let recipeLink = createElement('a');
        recipeLink.innerText = 'No recipes to display';
        recipeLink.id = 'noRecipeDropdown';
        recipeDropdown.appendChild(recipeLink);
        return;
    }

    for (let i in recipes)
    {
        let recipe = recipes[i];

        let recipeLink = createElement('a');
        recipeLink.href = `/recipe?id=${recipe.id}`;
        recipeLink.innerText = recipe.title;
        recipeDropdown.appendChild(recipeLink);
    }
}

function closeModal(modal)
{
    hideElement('invalidRecipeNameError');
    hideElement('postRecipeCreated');
    showElement('preRecipeCreated');
    modal.style.display = "none";
    getElementById('recipeName').value = '';
}

function createRecipe(recipe)
{
    fetch ("http://localhost:8080/api/recipe", {
        method: 'POST',
        headers: {
            "Content-Type": "application/json"
        },
        credentials: "same-origin",
        body: JSON.stringify(recipe)
    }).then(
        function (response)
        {
            if (response.status !== 201)
            {
                response.text().then(function(data) {
                    getElementById('createRecipeError').innerText = data;
                    showElement('createRecipeError');
                });

                return;
            }

            response.json().then(function(recipe) {
                getElementById('newRecipe').href = `/recipe?id=${recipe.id}`
            });

            hideElement('preRecipeCreated');
            showElement('postRecipeCreated');
            getRecipes();
        }
    ).catch(
        function (error)
        {
            getElementById("createRecipeError").value = error;
            showElement('createRecipeError');
        }
    );
}

function enableAutocomplete(recipes)
{
    let searchBar = getElementById('searchForRecipe');

    searchBar.addEventListener("input", function(e)
    {
        let matchedRecipes = [];

        for (let i = 0; i < recipes.length; i++)
        {
            if (recipes[i].title.substr(0, this.value.length).toUpperCase() === this.value.toUpperCase())
            {
                matchedRecipes.push(recipes[i]);
            }
        }

        displayRecipes(matchedRecipes);
    });
}
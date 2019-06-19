let createRecipeEnabled = true;

document.addEventListener("DOMContentLoaded", function(event)
{
    let modal = getElementById('createRecipeModal');
    let createRecipeButton = getElementById("createRecipeButton");
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
    let success = function(recipes) {
        displayRecipes(recipes);
        enableAutocomplete(recipes);
    };

    let failure = function(failure) {
        // TODO show error banner
    };

    callApi("/api/recipe", HTTP_GET, null, true, success, failure);
}

function displayRecipes(recipes)
{
    let allRecipes = getElementById('allRecipes');

    removeChildElements(allRecipes);

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

    let cookTime = createElement('label');
    cookTime.innerText = `Total time: ${recipe.totalTime}`;

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
    let success = function(response) {
        getElementById('newRecipe').href = `/recipe?id=${response.id}`;

        hideElement('preRecipeCreated');
        showElement('postRecipeCreated');
        getRecipes();

        createRecipeEnabled = true;
    };

    let failure = function(failure) {
        getElementById('createRecipeError').innerText = failure;
        showElement('createRecipeError');

        createRecipeEnabled = true;
    };

    if (createRecipeEnabled) {
        createRecipeEnabled = false;
        callApi("/api/recipe", HTTP_POST, recipe, true, success, failure);
    }
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
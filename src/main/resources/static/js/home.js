document.addEventListener("DOMContentLoaded", function() {
    Home.init();
});

export const Home = (function () {
    let createRecipeEnabled = true;

    const addEventListeners = function () {
        const modal = getElementById('createRecipeModal');
        const createRecipeButton = getElementById("createRecipeButton");
        const closeModalButton = document.getElementsByClassName("close")[0];
        const confirmRecipeButton = getElementById('confirmRecipeButton');

        createRecipeButton.onclick = function() {
            modal.style.display = "block";
        };

        closeModalButton.onclick = function() {
            closeModal(modal);
        };

        window.onclick = function(event) {
            if (event.target === modal) {
                closeModal(modal);
            }
        };

        window.onkeydown = function(event) {
            if (event.key === 'Escape') {
                closeModal(modal);
            }
        };

        confirmRecipeButton.onclick = function () {
            createRecipe();
        };

        getElementById('recipeName').addEventListener("keyup", function (event) {
            if (event.key === "Enter") {
                createRecipe();
            }
        });

        getRecipes();
    };

    const getRecipes = function () {
        const success = function(recipes) {
            displayRecipes(recipes);
            enableAutocomplete(recipes);
        };

        const failure = function(failure) {};

        callApi("/api/recipe", HTTP_GET, null, true, success, failure);
    };

    const displayRecipes = function (recipes) {
        const allRecipes = getElementById('allRecipes');

        removeChildElements(allRecipes);

        for (const i in recipes) {
            displayRecipe(recipes[i]);
        }
    };

    const displayRecipe = function (recipe) {
        const template = getTemplate('recipeTemplate');

        const recipeLink = template.querySelector('.recipeLink');
        const totalTime = template.querySelector('.totalTime');
        const recipeTitle = template.querySelector('.recipeTitle');
        const stars = template.querySelectorAll('.star');

        recipeLink.href = `/recipe?id=${recipe.id}`;
        totalTime.innerText = `Total time: ${recipe.totalTime}`;
        recipeTitle.innerText = recipe.title;

        for (let i = 0; i < recipe.rating; i++) {
            stars[i].classList.add('checked');
        }

        getElementById('allRecipes').appendChild(template);
    };

    const closeModal = function (modal) {
        hideElement('invalidRecipeNameError');
        hideElement('postRecipeCreated');
        showElement('preRecipeCreated');
        modal.style.display = "none";
        getElementById('recipeName').value = '';
    };

    const createRecipe = function () {
        hideElement('invalidRecipeNameError');

        const recipe = {
            title: getValueById('recipeName')
        };

        if (!validateStringLength(recipe.title, 1)) {
            showElement('invalidRecipeNameError');
            return;
        }

        const success = function(response) {
            getElementById('newRecipe').href = `/recipe?id=${response.id}`;

            hideElement('preRecipeCreated');
            showElement('postRecipeCreated');
            getRecipes();

            createRecipeEnabled = true;
        };

        const failure = function(failure) {
            getElementById('createRecipeError').innerText = failure;
            showElement('createRecipeError');

            createRecipeEnabled = true;
        };

        if (createRecipeEnabled) {
            createRecipeEnabled = false;
            callApi("/api/recipe", HTTP_POST, recipe, true, success, failure);
        }
    };

    const enableAutocomplete = function (recipes) {
        const searchBar = getElementById('searchForRecipe');

        searchBar.addEventListener("input", function() {
            const matchedRecipes = [];

            for (let i = 0; i < recipes.length; i++) {
                if (recipes[i].title.substr(0, this.value.length).toUpperCase() === this.value.toUpperCase()) {
                    matchedRecipes.push(recipes[i]);
                }
            }

            displayRecipes(matchedRecipes);
        });
    };

    const removeChildElements = function (element) {
        while (element.firstChild) {
            element.removeChild(element.firstChild);
        }
    };

    return {
        init: function () {
            addEventListeners();
        }
    }
})();

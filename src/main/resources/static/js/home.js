document.addEventListener("DOMContentLoaded", function() {
    Home.init();
});

Home = (function () {
    let createRecipeEnabled = true;

    let addEventListeners = function () {
        let modal = getElementById('createRecipeModal');
        let createRecipeButton = getElementById("createRecipeButton");
        let closeModalButton = document.getElementsByClassName("close")[0];
        let confirmRecipeButton = getElementById('confirmRecipeButton');

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

    let getRecipes = function () {
        let success = function(recipes) {
            displayRecipes(recipes);
            enableAutocomplete(recipes);
        };

        let failure = function(failure) {};

        callApi("/api/recipe", HTTP_GET, null, true, success, failure);
    };

    let displayRecipes = function (recipes) {
        let allRecipes = getElementById('allRecipes');

        removeChildElements(allRecipes);

        for (let i in recipes) {
            displayRecipe(recipes[i]);
        }
    };

    let displayRecipe = function (recipe) {
        let template = getTemplate('recipeTemplate');

        let recipeLink = template.querySelector('.recipeLink');
        let totalTime = template.querySelector('.totalTime');
        let recipeTitle = template.querySelector('.recipeTitle');
        let stars = template.querySelectorAll('.star');

        recipeLink.href = `/recipe?id=${recipe.id}`;
        totalTime.innerText = `Total time: ${recipe.totalTime}`;
        recipeTitle.innerText = recipe.title;

        for (let i = 0; i < recipe.rating; i++) {
            stars[i].classList.add('checked');
        }

        getElementById('allRecipes').appendChild(template);
    };

    let closeModal = function (modal) {
        hideElement('invalidRecipeNameError');
        hideElement('postRecipeCreated');
        showElement('preRecipeCreated');
        modal.style.display = "none";
        getElementById('recipeName').value = '';
    };

    let createRecipe = function () {
        hideElement('invalidRecipeNameError');

        let recipe = {
            title: getValueById('recipeName')
        };

        if (!validateStringLength(recipe.title, 1)) {
            showElement('invalidRecipeNameError');
            return;
        }

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
    };

    let enableAutocomplete = function (recipes) {
        let searchBar = getElementById('searchForRecipe');

        searchBar.addEventListener("input", function(e) {
            let matchedRecipes = [];

            for (let i = 0; i < recipes.length; i++) {
                if (recipes[i].title.substr(0, this.value.length).toUpperCase() === this.value.toUpperCase()) {
                    matchedRecipes.push(recipes[i]);
                }
            }

            displayRecipes(matchedRecipes);
        });
    };

    let removeChildElements = function (element) {
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

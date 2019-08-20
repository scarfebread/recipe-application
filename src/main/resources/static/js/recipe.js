document.addEventListener("DOMContentLoaded", function() {
    Recipe.init();
    RecipeEditor.init();
    DeleteIngredient.init();
    AddIngredient.init();
    AddStep.init();
    ShoppingListIntegration.init();
    IngredientFormatSlider.init();
    ShareRecipe.init();
    DeleteRecipe.init();
    RecipeRating.init();

    EventLog.add('Recipe loaded');
});

Recipe = (function () {
    let addEventListeners = function () {
        let difficulty = getElementById('difficulty');
        let difficultyLabel = getElementById('difficultyLabel');
        let cookTime = getElementById('cookTime');
        let prepTime = getElementById('prepTime');
        let cookTimeLabel = getElementById('cookTimeLabel');
        let prepTimeLabel = getElementById('prepTimeLabel');
        let serves = getElementById('serves');
        let servesLabel = getElementById('servesLabel');
        let notes = getElementById('notes');
        let instructionsModal = getElementById('instructionsModal');

        serves.value = recipeServes;
        difficulty.value = recipeDifficulty;

        difficulty.onchange = function () {
            difficultyLabel.innerHTML = difficulty.value;
            update();
        };

        cookTime.onchange = function () {
            cookTimeLabel.innerText = cookTime.value;
            update();
        };

        prepTime.onchange = function () {
            prepTimeLabel.innerText = prepTime.value;
            update();
        };

        serves.onchange = function () {
            servesLabel.innerText = serves.value;
            update();
        };

        notes.onchange = function () {
            update();
        };

        getElementById('closeInstructions').onclick = function () {
            closeModal(instructionsModal);
        };

        if (displayInstructions) {
            instructionsModal.style.display = "block";
        }

        window.onclick = function(event) {
            if (event.target === instructionsModal) {
                closeModal(instructionsModal);
            }
        };

        window.onkeydown = function(event) {
            if (event.key === 'Escape') {
                closeModal(instructionsModal);
            }
        };
    };

    let update = function () {
        let serves = getElementById('serves');
        let difficulty = getElementById('difficulty');

        let recipe = {
            id: recipeId,
            notes: getValueById('notes'),
            rating: recipeRating,
            serves: serves.options[serves.selectedIndex].value,
            cookTime: getElementById('cookTime').value,
            prepTime: getElementById('prepTime').value,
            difficulty: difficulty.options[difficulty.selectedIndex].value
        };

        let success = function() {
            EventLog.add('Recipe updated')
        };

        let failure = function(failure) {
            EventLog.add('Recipe failed to update')
        };

        callApi("/api/recipe", HTTP_PUT, recipe, false, success, failure);
    };

    let closeModal = function (modal) {
        modal.style.display = "none";
    };

    return {
        init: function () {
            autocomplete(getElementById("ingredientDescription"), ingredients);
            addEventListeners();
        },

        update: function () {
            update();
        }
    }
})();

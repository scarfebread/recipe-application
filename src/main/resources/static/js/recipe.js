import {AddIngredient} from "./add-ingredient.js";
import {RecipeEditor} from "./recipe-editor.js";
import {DeleteIngredient} from "./delete-ingredient.js";
import {AddStep} from "./add-step.js";
import {ShoppingListIntegration} from "./shopping-list-integration.js";
import {IngredientFormatSlider} from "./ingredient-format-slider.js";
import {ShareRecipe} from "./share-recipe.js";
import {DeleteRecipe} from "./delete-recipe.js";
import {EventLog} from "./event-log.js";

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

export const Recipe = (function () {
    const addEventListeners = function () {
        const difficulty = getElementById('difficulty');
        const difficultyLabel = getElementById('difficultyLabel');
        const cookTime = getElementById('cookTime');
        const prepTime = getElementById('prepTime');
        const cookTimeLabel = getElementById('cookTimeLabel');
        const prepTimeLabel = getElementById('prepTimeLabel');
        const serves = getElementById('serves');
        const servesLabel = getElementById('servesLabel');
        const notes = getElementById('notes');
        const instructionsModal = getElementById('instructionsModal');

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

    const update = function () {
        const serves = getElementById('serves');
        const difficulty = getElementById('difficulty');

        const recipe = {
            id: recipeId,
            notes: getValueById('notes'),
            rating: recipeRating,
            serves: serves.options[serves.selectedIndex].value,
            cookTime: getElementById('cookTime').value,
            prepTime: getElementById('prepTime').value,
            difficulty: difficulty.options[difficulty.selectedIndex].value
        };

        const success = function() {
            EventLog.add('Recipe updated')
        };

        const failure = function(failure) {
            EventLog.add('Recipe failed to update')
        };

        callApi("/api/recipe", HTTP_PUT, recipe, false, success, failure);
    };

    const closeModal = function (modal) {
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

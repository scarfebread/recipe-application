let rating;
let recipeDeleted = false;

document.addEventListener("DOMContentLoaded", function()
{
    RecipeEditor.init();
    DeleteIngredient.init();
    AddIngredient.init();
    AddStep.init();
    ShoppingList.init();
    IngredientFormatSlider.init();
    ShareRecipe.init();

    rating = recipeRating;

    let shareRecipeButton = getElementById('shareRecipeButton');
    let shareRecipeModal = getElementById('shareRecipeModal');
    let instructionsModal = getElementById('instructionsModal');
    let closeShareRecipeModalButton = getElementById('closeShareRecipeModal');

    let deleteRecipeButton = getElementById('deleteRecipeButton');
    let deleteRecipeModal = getElementById('deleteRecipeModal');
    let closeDeleteRecipeModalButton = getElementById('closeDeleteRecipeModal');
    let deleteRecipeLabel = getElementById('deleteLabel');

    shareRecipeButton.onclick = function() {
        shareRecipeModal.style.display = "block";
    };

    closeShareRecipeModalButton.onclick = function() {
        closeModal(shareRecipeModal);
    };

    deleteRecipeButton.onclick = function() {
        deleteRecipeLabel.innerText = `Are you sure you want to delete ${recipeTitle}?`;
        deleteRecipeModal.style.display = "block";
    };

    closeDeleteRecipeModalButton.onclick = function() {
        closeModal(deleteRecipeModal);
    };

    getElementById('closeInstructions').onclick = function () {
        closeModal(instructionsModal);
    };

    if (displayInstructions) {
        instructionsModal.style.display = "block";
    }

    window.onclick = function(event) {
        if (event.target === shareRecipeModal) {
            closeModal(shareRecipeModal);
        }

        if (event.target === deleteRecipeModal) {
            closeModal(deleteRecipeModal);
        }

        if (event.target === instructionsModal) {
            closeModal(instructionsModal);
        }
    };

    window.onkeydown = function(event)
    {
        if (event.key === 'Escape') {
            closeModal(shareRecipeModal);
            closeModal(deleteRecipeModal);
        }
    };

    let confirmDeleteRecipe = getElementById('confirmDeleteButton');
    let difficulty = getElementById('difficulty');
    let difficultyLabel = getElementById('difficultyLabel');
    let cookTime = getElementById('cookTime');
    let prepTime = getElementById('prepTime');
    let cookTimeLabel = getElementById('cookTimeLabel');
    let prepTimeLabel = getElementById('prepTimeLabel');
    let serves = getElementById('serves');
    let servesLabel = getElementById('servesLabel');
    let notes = getElementById('notes');
    let rating1 = getElementById('rating1');
    let rating2 = getElementById('rating2');
    let rating3 = getElementById('rating3');
    let rating4 = getElementById('rating4');
    let rating5 = getElementById('rating5');

    displayRating();
    serves.value = recipeServes;
    difficulty.value = recipeDifficulty;

    difficulty.onchange = function () {
        difficultyLabel.innerHTML = difficulty.value;
        updateRecipe();
    };

    cookTime.onchange = function () {
        cookTimeLabel.innerText = cookTime.value;
        updateRecipe();
    };

    prepTime.onchange = function () {
        prepTimeLabel.innerText = prepTime.value;
        updateRecipe();
    };

    serves.onchange = function () {
        servesLabel.innerText = serves.value;
        updateRecipe();
    };

    notes.onchange = function () {
        updateRecipe();
    };

    rating1.onclick = function () {
        rating = 1;
        displayRating();
        updateRecipe()
    };

    rating2.onclick = function () {
        rating = 2;
        displayRating();
        updateRecipe()
    };

    rating3.onclick = function () {
        rating = 3;
        displayRating();
        updateRecipe()
    };

    rating4.onclick = function () {
        rating = 4;
        displayRating();
        updateRecipe()
    };

    rating5.onclick = function () {
        rating = 5;
        displayRating();
        updateRecipe()
    };

    confirmDeleteRecipe.onclick = function () {
        confirmDeleteRecipe.disabled = true;

        hideElement('deleteRecipeError');

        deleteRecipe();

        if (!recipeDeleted) {
            confirmDeleteRecipe.disabled = false;
        }
    };

    autocomplete(getElementById("ingredientDescription"), ingredients);
});

function updateRecipe()
{
    let serves = getElementById('serves');
    let difficulty = getElementById('difficulty');

    let recipe = {
        id: recipeId,
        notes: getValueById('notes'),
        rating: rating,
        serves: serves.options[serves.selectedIndex].value,
        cookTime: getElementById('cookTime').value,
        prepTime: getElementById('prepTime').value,
        difficulty: difficulty.options[difficulty.selectedIndex].value,
        steps: getSteps()
    };

    let success = function() {};

    let failure = function(failure) {};

    callApi("/api/recipe", HTTP_PUT, recipe, false, success, failure);
}

function getSteps()
{
    let steps = [];

    let stepTable = getElementById('stepTable');

    for (let i = 1, row; row = stepTable.rows[i]; i++) {
        if (i === stepTable.rows.length -1)
        {
            break;
        }

        row.children[0].children[0].innerHTML = (i) + '.';

        steps.push(row.children[1].innerHTML)
    }

    return steps;
}

function deleteRecipe()
{
    let recipe = {
        id: recipeId
    };

    let success = function() {
        hideElement('preDelete');
        showElement('postDelete');

        recipeDeleted = true;
    };

    let failure = function(failure) {
        getElementById('deleteRecipeError').innerText = failure;
        showElement('deleteRecipeError');
    };

    callApi("/api/recipe", HTTP_DELETE, recipe, false, success, failure);
}

function displayRating()
{
    let starRating = getElementById('starRating');

    let children = starRating.children;
    for (let i = 0; i < children.length; i++)
    {
        let star = children[i];

        star.classList.remove('checked');

        if (i < rating)
        {
            star.classList.add('checked');
        }
    }
}

function closeModal(modal)
{
    if (recipeDeleted) {
        return;
    }

    hideElement('invalidUsernameError');
    hideElement('shareRecipeError');
    hideElement('postShare');
    showElement('preShare');
    modal.style.display = "none";
    getElementById('username').value = '';
}

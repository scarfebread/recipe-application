let rating;
let recipeDeleted = false;

document.addEventListener("DOMContentLoaded", function(event)
{
    RecipeEditor.init();
    DeleteIngredient.init();
    AddIngredient.init();
    ShoppingList.init();
    IngredientFormatSlider.init();

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

    let confirmShareRecipe = getElementById('confirmShareButton');
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
    let addStepButton = getElementById('addStepButton');

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

    confirmShareRecipe.onclick = function () {
        confirmShareRecipe.disabled = true;

        hideElement('invalidUsernameError');
        hideElement('shareRecipeError');

        let username = getValueById('username');

        if (validateStringLength(username, 1)) {
            shareRecipe(username);
        } else {
            showElement('invalidUsernameError');
        }

        confirmShareRecipe.disabled = false;
    };

    confirmDeleteRecipe.onclick = function () {
        confirmDeleteRecipe.disabled = true;

        hideElement('deleteRecipeError');

        deleteRecipe();

        if (!recipeDeleted) {
            confirmDeleteRecipe.disabled = false;
        }
    };

    addStepButton.onclick = function () {
        createStep();
    };

    getElementById('newStep').addEventListener("keyup", function (event) {
        if (event.key === "Enter") {
            createStep();
        }
    });

    addStepDeleteListeners();
    addStepEditListeners();

    autocomplete(getElementById("ingredientDescription"), ingredients);
});

function addStepEditListeners()
{
    Array.from(document.getElementsByClassName('stepColumn')).forEach(function(element) {
        addEditListener(element);
    });
}

function addEditListener(element)
{
    element.addEventListener("keydown", function (event) {
        if (event.key === "Enter") {
            event.preventDefault();
            element.blur();
        }
    });

    element.addEventListener('blur', function () {
        if (!validateStringLength(element.innerHTML, 1)) {
            let row = element.parentNode;
            let table = row.parentNode;

            table.removeChild(row);
        }

        updateRecipe();
    });
}

function addStepDeleteListeners()
{
    Array.from(document.getElementsByClassName('stepDelete')).forEach(function(element) {
        addStepDeleteListener(element);
    });
}

function addStepDeleteListener(element)
{
    element.addEventListener('click', function () {
        let row = element.parentNode.parentNode;
        let table = row.parentNode;

        table.removeChild(row);

        updateRecipe();
    });
}

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

function shareRecipe(newUser)
{
    let recipe = {
        id: recipeId,
        newUser: newUser
    };

    let success = function() {
        hideElement('preShare');
        showElement('postShare');
    };

    let failure = function(failure) {
        getElementById('shareRecipeError').innerText = failure;
        showElement('shareRecipeError');
    };

    callApi("/api/recipe/share", HTTP_POST, recipe, false, success, failure);
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

function createStep()
{
    let newStep = getElementById('newStep');

    if (!validateStringLength(newStep.value, 1)) {
        return;
    }

    addStepToList(newStep.value);
    updateRecipe();

    newStep.value = '';
}

function addStepToList(step)
{
    let stepTable = getElementById('stepTable').children[0];

    let template = getTemplate('stepTemplate');

    let stepNumber = template.querySelector('.stepNumber');
    let stepColumn = template.querySelector('.stepColumn');
    let stepActionColumn = template.querySelector('.stepActionColumn');
    let stepDelete = template.querySelector('.stepDelete');

    stepNumber.innerText = (getSteps().length + 1) + '.';
    stepColumn.innerText = step;
    stepActionColumn.style.display = 'flex';

    stepTable.insertBefore(template, stepTable.children[stepTable.children.length -1]);

    addEditListener(stepColumn);
    addStepDeleteListener(stepDelete);
}

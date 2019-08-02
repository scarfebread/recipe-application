let rating;
let recipeDeleted = false;
let editRecipe = false;

document.addEventListener("DOMContentLoaded", function(event)
{
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
        if (event.key === 'Escape')
        {
            closeModal(shareRecipeModal);
            closeModal(deleteRecipeModal);
        }
    };

    let confirmShareRecipe = getElementById('confirmShareButton');
    let confirmDeleteRecipe = getElementById('confirmDeleteButton');
    let difficulty = getElementById('difficulty');
    let cookTime = getElementById('cookTime');
    let prepTime = getElementById('prepTime');
    let serves = getElementById('serves');
    let notes = getElementById('notes');
    let rating1 = getElementById('rating1');
    let rating2 = getElementById('rating2');
    let rating3 = getElementById('rating3');
    let rating4 = getElementById('rating4');
    let rating5 = getElementById('rating5');
    let editRecipeButton = getElementById('editRecipeButton');
    let lockRecipeButton = getElementById('lockRecipeButton');
    let addIngredientButton = getElementById('addIngredientButton');
    let addStepButton = getElementById('addStepButton');

    displayRating();
    serves.value = recipeServes;
    difficulty.value = recipeDifficulty;
    cookTime.innerText = recipeCookTime;
    prepTime.innerText = recipePrepTime;

    difficulty.onchange = function () {
        updateRecipe();
    };

    cookTime.onchange = function () {
        updateRecipe();
    };

    prepTime.onchange = function () {
        updateRecipe();
    };

    serves.onchange = function () {
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

    editRecipeButton.onclick = function () {
        editRecipe = true;
        editRecipeButton.style.display = 'none';
        lockRecipeButton.style.display = 'inline-block';
        difficulty.disabled = false;
        cookTime.disabled = false;
        prepTime.disabled = false;
        serves.disabled = false;
        notes.disabled = false;
        getElementById('addIngredientRow').hidden = false;
        getElementById('addStepRow').hidden = false;

        let nonEditableIds = ['addIngredientColumn', 'addQuantityColumn', 'ingredientHeading', 'quantityHeading'];

        Array.from(document.getElementsByClassName('ingredientColumn')).forEach(function (element) {
            if (!nonEditableIds.includes(element.id)) {
                element.contentEditable = true;
                element.style.cursor = 'pointer';
            }

            element.style.width = '45%';
        });

        Array.from(document.getElementsByClassName('quantityColumn')).forEach(function (element) {
            if (!nonEditableIds.includes(element.id)) {
                element.contentEditable = true;
                element.style.cursor = 'pointer';
            }

            element.style.width = '28%';
        });

        Array.from(document.getElementsByClassName('stepColumn')).forEach(function (element) {
            element.contentEditable = true;
            element.style.width = '70%';
            element.style.cursor = 'pointer';
        });

        Array.from(document.getElementsByClassName('ingredientActionColumn')).forEach(function(element) {
            element.style.display = 'flex';
        });

        Array.from(document.getElementsByClassName('buttonColumn')).forEach(function (element) {
            element.hidden = false;
        });

        Array.from(document.getElementsByClassName('stepHeader')).forEach(function (element) {
            element.style.width = '65%';
        });
    };

    lockRecipeButton.onclick = function() {
        editRecipe = false;
        editRecipeButton.style.display = 'inline-block';
        lockRecipeButton.style.display = 'none';
        difficulty.disabled = true;
        cookTime.disabled = true;
        prepTime.disabled = true;
        serves.disabled = true;
        notes.disabled = true;
        getElementById('addIngredientRow').hidden = true;
        getElementById('addStepRow').hidden = true;

        Array.from(document.getElementsByClassName('ingredientColumn')).forEach(function(element) {
            element.contentEditable = false;
            element.style.width = '60%';
            element.style.cursor = 'auto';
        });

        Array.from(document.getElementsByClassName('quantityColumn')).forEach(function(element) {
            element.contentEditable = false;
            element.style.width = '40%';
            element.style.cursor = 'auto';
        });

        Array.from(document.getElementsByClassName('stepColumn')).forEach(function(element) {
            element.contentEditable = false;
            element.style.width = '95%';
            element.style.cursor = 'auto';
        });

        Array.from(document.getElementsByClassName('ingredientActionColumn')).forEach(function(element) {
            element.style.display = 'none';
        });

        Array.from(document.getElementsByClassName('buttonColumn')).forEach(function(element) {
            element.hidden = true;
        });

        Array.from(document.getElementsByClassName('stepHeader')).forEach(function(element) {
            element.style.width = '95%';
        });
    };

    addIngredientButton.onclick = function () {
        createIngredient();
    };

    addCreateIngredientEnterKeyEventListener(getElementById('ingredientDescription'));
    addCreateIngredientEnterKeyEventListener(getElementById('ingredientQuantity'));

    addStepButton.onclick = function () {
        createStep();
    };

    getElementById('newStep').addEventListener("keyup", function (event) {
        if (event.key === "Enter") {
            createStep();
        }
    });

    addIngredientDeleteListeners();
    addIngredientEditListeners();
    addStepEditListeners();
    addShoppingListEventListeners();

    autocomplete(getElementById("ingredientDescription"), ingredients);
});

function addIngredientEditListeners()
{
    Array.from(document.getElementsByClassName('ingredientColumn')).forEach(function(element) {
        addEditListener(element);
    });

    Array.from(document.getElementsByClassName('quantityColumn')).forEach(function(element) {
        addEditListener(element);
    });
}

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

function addIngredientDeleteListeners()
{
    Array.from(document.getElementsByClassName('ingredientDelete')).forEach(function(element) {
        addIngredientDeleteListener(element);
    });
}

function addIngredientDeleteListener(element)
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
        ingredients: getIngredients(),
        steps: getSteps()
    };

    let success = function() {};

    let failure = function(failure) {
        // TODO show error banner
    };

    callApi("/api/recipe", HTTP_PUT, recipe, false, success, failure);
}

function getIngredients()
{
    let ingredients = [];

    let ingredientTable = getElementById('ingredientTable');

    for (let i = 1, row; row = ingredientTable.rows[i]; i++) {
        if (i === ingredientTable.rows.length -1) {
            break;
        }

        let tableRow = metric ? 1 : 2;

        ingredients.push({
            description: row.children[0].innerHTML,
            quantity: row.children[tableRow].innerHTML
        });
    }

    return ingredients;
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

function createIngredient()
{
    let ingredientDescription = getElementById('ingredientDescription');
    let ingredientQuantity = getElementById('ingredientQuantity');

    if (!validateStringLength(ingredientDescription.value, 1)) {
        return;
    }

    let ingredient = {
        recipe: recipeId,
        description: ingredientDescription.value,
        quantity: ingredientQuantity.value
    };

    let success = function(ingredient) {
        addIngredientToList(ingredient);
        ingredientDescription.value = '';
        ingredientQuantity.value = '';
    };

    let failure = function(failure) {};

    callApi("/api/recipe/ingredient", HTTP_PUT, ingredient, true, success, failure);
}

function addIngredientToList(ingredient)
{
    let template = getTemplate('ingredientTemplate');

    let descriptionColumn = template.querySelector('.ingredientColumn');
    let metricColumn = template.querySelector('.metric');
    let imperialColumn = template.querySelector('.imperial');
    let ingredientDelete = template.querySelector('.ingredientDelete');
    let ingredientActionColumn = template.querySelector('.ingredientActionColumn');
    let shoppingCartSymbol = template.querySelector('.shoppingCartSymbol');

    descriptionColumn.innerText = ingredient.description;
    metricColumn.innerText = ingredient.metric;
    imperialColumn.innerText = ingredient.imperial;
    ingredientActionColumn.style.display = 'flex';
    shoppingCartSymbol.setAttribute('data-ingredientId', ingredient.id);

    if (metric) {
        imperialColumn.style.display = 'none';
    } else {
        metricColumn.style.display = 'none';
    }

    let ingredientTable = getElementById('ingredientTable').children[0];
    ingredientTable.insertBefore(template, ingredientTable.children[ingredientTable.children.length -1]);

    addEditListener(descriptionColumn);
    addEditListener(metricColumn);
    addEditListener(imperialColumn);
    addIngredientDeleteListener(ingredientDelete);
    addShoppingListEventListener(shoppingCartSymbol);
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
    let ingredientActionColumn = template.querySelector('.ingredientActionColumn');
    let stepDelete = template.querySelector('.ingredientDelete');

    stepNumber.innerText = (getSteps().length + 1) + '.';
    stepColumn.innerText = step;
    ingredientActionColumn.style.display = 'flex';

    stepTable.insertBefore(template, stepTable.children[stepTable.children.length -1]);

    addEditListener(stepColumn);
    addIngredientDeleteListener(stepDelete);
}

function addCreateIngredientEnterKeyEventListener(element)
{
    element.addEventListener("keyup", function (event) {
        if (event.key === "Enter") {
            createIngredient();
        }
    });
}

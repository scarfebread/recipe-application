let rating;
let recipeDeleted = false;
let editRecipe = false;

document.addEventListener("DOMContentLoaded", function(event)
{
    rating = recipeRating;

    let shareRecipeButton = getElementById('shareRecipeButton');
    let shareRecipeModal = getElementById('shareRecipeModal');
    let closeShareRecipeModalButton = getElementById('closeShareRecipeModal');

    let deleteRecipeButton = getElementById('deleteRecipeButton');
    let deleteRecipeModal = getElementById('deleteRecipeModal');
    let closeDeleteRecipeModalButton = getElementById('closeDeleteRecipeModal');
    let deleteRecipeLabel = getElementById('deleteLabel');

    shareRecipeButton.onclick = function()
    {
        shareRecipeModal.style.display = "block";
    };

    closeShareRecipeModalButton.onclick = function()
    {
        closeModal(shareRecipeModal);
    };

    deleteRecipeButton.onclick = function()
    {
        deleteRecipeLabel.innerText = `Are you sure you want to delete ${recipeTitle}?`;
        deleteRecipeModal.style.display = "block";
    };

    closeDeleteRecipeModalButton.onclick = function()
    {
        closeModal(deleteRecipeModal);
    };

    window.onclick = function(event)
    {
        if (event.target === shareRecipeModal)
        {
            closeModal(shareRecipeModal);
        }

        if (event.target === deleteRecipeModal)
        {
            closeModal(deleteRecipeModal);
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
    let addIngredientButton = getElementById('addIngredientButton');
    let addStepButton = getElementById('addStepButton');

    displayRating();
    serves.value = recipeServes;
    difficulty.value = recipeDifficulty;
    cookTime.innerText = recipeCookTime;
    prepTime.innerText = recipePrepTime;

    difficulty.onchange = function ()
    {
        updateRecipe();
    };

    cookTime.addEventListener('keypress', preventReturn(event));
    prepTime.addEventListener('keypress', preventReturn(event));

    cookTime.addEventListener("blur", function ()
    {
        updateRecipe();
    });

    prepTime.addEventListener("blur", function ()
    {
        updateRecipe();
    });

    serves.onchange = function ()
    {
        updateRecipe();
    };

    notes.onchange = function ()
    {
        updateRecipe();
    };

    rating1.onclick = function ()
    {
        rating = 1;
        displayRating();
        updateRecipe()
    };

    rating2.onclick = function ()
    {
        rating = 2;
        displayRating();
        updateRecipe()
    };

    rating3.onclick = function ()
    {
        rating = 3;
        displayRating();
        updateRecipe()
    };

    rating4.onclick = function ()
    {
        rating = 4;
        displayRating();
        updateRecipe()
    };

    rating5.onclick = function ()
    {
        rating = 5;
        displayRating();
        updateRecipe()
    };

    confirmShareRecipe.onclick = function ()
    {
        confirmShareRecipe.disabled = true;

        hideElement('invalidUsernameError');
        hideElement('shareRecipeError');

        let username = getValueById('username');

        if (validateStringLength(username, 1))
        {
            shareRecipe(username);
        }
        else
        {
            showElement('invalidUsernameError');
        }

        confirmShareRecipe.disabled = false;
    };

    confirmDeleteRecipe.onclick = function ()
    {
        confirmDeleteRecipe.disabled = true;

        hideElement('deleteRecipeError');

        deleteRecipe();

        if (!recipeDeleted)
        {
            confirmDeleteRecipe.disabled = false;
        }
    };

    editRecipeButton.onclick = function ()
    {
        editRecipeButton.disabled = true;

        editRecipe = !editRecipe;

        if (editRecipe)
        {
            editRecipeButton.innerText = 'LOCK';
            difficulty.disabled = false;
            cookTime.contentEditable = true;
            prepTime.contentEditable = true;
            serves.disabled = false;
            notes.disabled = false;
            getElementById('addIngredientRow').hidden = false;
            getElementById('addStepRow').hidden = false;

            Array.from(document.getElementsByClassName('ingredients')).forEach(function(element) {
                element.contentEditable = true;
            });

            Array.from(document.getElementsByClassName('steps')).forEach(function(element) {
                element.contentEditable = true;
                element.style.width = '78%';
            });

            Array.from(document.getElementsByClassName('ingredientDelete')).forEach(function(element) {
                element.hidden = false;
                element.style.display = 'inline-block';
            });
        }
        else
        {
            editRecipeButton.innerText = 'EDIT';
            difficulty.disabled = true;
            cookTime.contentEditable = false;
            prepTime.contentEditable = false;
            serves.disabled = true;
            notes.disabled = true;
            getElementById('addIngredientRow').hidden = true;
            getElementById('addStepRow').hidden = true;

            Array.from(document.getElementsByClassName('ingredients')).forEach(function(element) {
                element.contentEditable = false;
            });

            Array.from(document.getElementsByClassName('steps')).forEach(function(element) {
                element.contentEditable = false;
                element.style.width = '100%';
            });

            Array.from(document.getElementsByClassName('ingredientDelete')).forEach(function(element) {
                element.hidden = true;
                element.style.display = 'none';
            });
        }

        editRecipeButton.disabled = false;
    };

    addIngredientButton.onclick = function ()
    {
        let newIngredient = getElementById('newIngredient');

        if (!validateStringLength(newIngredient.value, 1))
        {
            return;
        }

        addIngredient(newIngredient.value);
        updateRecipe();

        newIngredient.value = '';
    };

    addStepButton.onclick = function ()
    {
        let newStep = getElementById('newStep');

        if (!validateStringLength(newStep.value, 1))
        {
            return;
        }

        addStep(newStep.value);
        updateRecipe();

        newStep.value = '';
    };

    addIngredientDeleteListeners();
    addIngredientEditListeners();
    addStepEditListeners();
});

function addIngredientEditListeners()
{
    Array.from(document.getElementsByClassName('ingredients')).forEach(function(element) {
        addEditListener(element);
    });
}

function addStepEditListeners()
{
    Array.from(document.getElementsByClassName('steps')).forEach(function(element) {
        addEditListener(element);
    });
}

function addEditListener(element)
{
    element.addEventListener('blur', function () {
        if (!validateStringLength(element.innerHTML, 1))
        {
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
        element.addEventListener('click', function () {
            let row = element.parentNode.parentNode;
            let table = row.parentNode;

            table.removeChild(row);

            updateRecipe();
        });
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
        cookTime: getElementById('cookTime').innerText,
        prepTime: getElementById('prepTime').innerText,
        difficulty: difficulty.options[difficulty.selectedIndex].value,
        ingredients: getIngredients(),
        steps: getSteps()
    };

    fetch ("http://localhost:8080/api/recipe", {
        method: 'PUT',
        headers: {
            "Content-Type": "application/json"
        },
        credentials: "same-origin",
        body: JSON.stringify(recipe)
    }).then(
        function (response) {
            if (response.status !== 202)
            {
                response.text().then(function(data) {
                    // TODO throw error
                });

                return false;
            }
        }
    ).catch(
        function (error) {
            // TODO show error banner
        }
    );
}

function getIngredients()
{
    let ingredients = [];

    let ingredientTable = getElementById('ingredientTable');

    for (let i = 0, row; row = ingredientTable.rows[i]; i++) {
        if (i === ingredientTable.rows.length -1)
        {
            break;
        }

        ingredients.push(row.children[0].innerHTML)
    }

    return ingredients;
}

function getSteps()
{
    let steps = [];

    let stepTable = getElementById('stepTable');

    for (let i = 0, row; row = stepTable.rows[i]; i++) {
        if (i === stepTable.rows.length -1)
        {
            break;
        }

        row.children[0].children[0].innerHTML = (i + 1) + '.';

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

    fetch ("http://localhost:8080/api/recipe/share", {
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
                    getElementById('shareRecipeError').innerText = data;
                    showElement('shareRecipeError');
                });

                return;
            }

            hideElement('preShare');
            showElement('postShare');
        }
    ).catch(
        function (error) {
            getElementById('shareRecipeError').innerText = error;
            showElement('shareRecipeError');
        }
    );
}

// TODO there's a lot of duplicate code with the fetch api. This should be refactored.
function deleteRecipe()
{
    let recipe = {
        id: recipeId
    };

    fetch ("http://localhost:8080/api/recipe", {
        method: 'DELETE',
        headers: {
            "Content-Type": "application/json"
        },
        credentials: "same-origin",
        body: JSON.stringify(recipe)
    }).then(
        function (response) {
            // TODO specifically checking status codes will break if the controller was changed
            if (response.status !== 202)
            {
                response.text().then(function(data) {
                    getElementById('deleteRecipeError').innerText = data;
                    showElement('deleteRecipeError');
                });

                return;
            }

            hideElement('preDelete');
            showElement('postDelete');

            recipeDeleted = true;
        }
    ).catch(
        function (error) {
            getElementById('deleteRecipeError').innerText = error;
            showElement('deleteRecipeError');
        }
    );
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

function preventReturn(event)
{
    if (event.which === 13)
    {
        event.preventDefault();
    }
}

function closeModal(modal)
{
    if (recipeDeleted)
    {
        return;
    }

    hideElement('invalidUsernameError');
    hideElement('shareRecipeError');
    hideElement('postShare');
    showElement('preShare');
    modal.style.display = "none";
    getElementById('username').value = '';
}

function addIngredient(ingredient)
{
    let ingredientTable = getElementById('ingredientTable').children[0];

    let row = createElement('tr');

    let ingredientColumn = createElement('td');
    ingredientColumn.innerText = ingredient;
    ingredientColumn.contentEditable = editRecipe;
    ingredientColumn.className = 'ingredients';

    let actionColumn = createElement('td');
    actionColumn.className = 'ingredientActionColumn';

    let deleteButton = createElement('span');
    deleteButton.classList.add('close');
    deleteButton.classList.add('ingredientDelete');
    deleteButton.innerText = '×';
    deleteButton.hidden = !editRecipe;

    if (editRecipe) {
        deleteButton.style.display = 'inline-block';
    } else {
        deleteButton.style.display = 'hidden'
    }

    actionColumn.appendChild(deleteButton);
    row.appendChild(ingredientColumn);
    row.appendChild(actionColumn);

    ingredientTable.insertBefore(row, ingredientTable.children[ingredientTable.children.length -1]);

    addEditListener(ingredientColumn);
    addIngredientDeleteListeners();
}

function addStep(step)
{
    let stepTable = getElementById('stepTable').children[0];

    let row = createElement('tr');

    let stepHeading = createElement('h3');
    stepHeading.innerText = (getSteps().length + 1) + '.';

    let stepNumberColumn = createElement('td');
    stepNumberColumn.appendChild(stepHeading);

    let stepColumn = createElement('td');
    stepColumn.innerText = step;
    stepColumn.contentEditable = editRecipe;
    stepColumn.classList.add('steps');
    stepColumn.classList.add('recipeTableRow');

    let actionColumn = createElement('td');
    actionColumn.className = 'ingredientActionColumn';

    let deleteButton = createElement('span');
    deleteButton.classList.add('close');
    deleteButton.classList.add('ingredientDelete');
    deleteButton.innerText = '×';
    deleteButton.hidden = !editRecipe;

    if (editRecipe) {
        deleteButton.style.display = 'inline-block';
    } else {
        deleteButton.style.display = 'hidden'
    }

    actionColumn.appendChild(deleteButton);
    row.appendChild(stepNumberColumn);
    row.appendChild(stepColumn);
    row.appendChild(actionColumn);

    stepTable.insertBefore(row, stepTable.children[stepTable.children.length -1]);

    addEditListener(stepColumn);
    addIngredientDeleteListeners();
}
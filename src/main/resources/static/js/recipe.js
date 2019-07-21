let rating;
let recipeDeleted = false;
let editRecipe = false;
let metric = true;

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
    let ingredientFormatButton = getElementById('ingredientFormatButton');

    ingredientFormatButton.onclick = function()
    {
        metric = !metric;

        let displayClass;
        let hideClass;

        if (metric) {
            displayClass = 'metric';
            hideClass = 'imperial';
        } else {
            displayClass = 'imperial';
            hideClass = 'metric';
        }

        Array.from(document.getElementsByClassName(hideClass)).forEach(function(element) {
            element.style.display = "none";
        });

        Array.from(document.getElementsByClassName(displayClass)).forEach(function(element) {
            element.style.display = "table-cell";
        });
    };

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

    cookTime.onchange = function ()
    {
        updateRecipe();
    };

    prepTime.onchange = function ()
    {
        updateRecipe();
    };

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
            cookTime.disabled = false;
            prepTime.disabled = false;
            serves.disabled = false;
            notes.disabled = false;
            getElementById('addIngredientRow').hidden = false;
            getElementById('addStepRow').hidden = false;

            Array.from(document.getElementsByClassName('ingredientColumn')).forEach(function(element) {
                if (element.id !== 'addIngredientColumn') {
                    element.contentEditable = true;
                }

                element.style.width = '45%';
            });

            Array.from(document.getElementsByClassName('quantityColumn')).forEach(function(element) {
                if (element.id !== 'addQuantityColumn') {
                    element.contentEditable = true;
                }

                element.style.width = '28%';
            });
            
            Array.from(document.getElementsByClassName('steps')).forEach(function(element) {
                element.contentEditable = true;
                element.style.width = '70%';
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
            cookTime.disabled = true;
            prepTime.disabled = true;
            serves.disabled = true;
            notes.disabled = true;
            getElementById('addIngredientRow').hidden = true;
            getElementById('addStepRow').hidden = true;

            Array.from(document.getElementsByClassName('ingredientColumn')).forEach(function(element) {
                element.contentEditable = false;
                element.style.width = '60%';
            });

            Array.from(document.getElementsByClassName('quantityColumn')).forEach(function(element) {
                element.contentEditable = false;
                element.style.width = '40%';
            });

            Array.from(document.getElementsByClassName('steps')).forEach(function(element) {
                element.contentEditable = false;
                element.style.width = '95%';
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
        let ingredientDescription = getElementById('ingredientDescription');
        let ingredientQuantity = getElementById('ingredientQuantity');

        if (!validateStringLength(ingredientDescription.value, 1))
        {
            return;
        }

        let ingredient = {
            recipe: recipeId,
            description: ingredientDescription.value,
            quantity: ingredientQuantity.value
        };

        let success = function(ingredient) {
            addIngredient(ingredient.description, ingredient.metric, ingredient.imperial);
            ingredientDescription.value = '';
            ingredientQuantity.value = '';
        };

        let failure = function(failure) {};

        callApi("/api/recipe/ingredient", HTTP_PUT, ingredient, true, success, failure);
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

function addIngredient(description, metric, imperial)
{
    let ingredientTable = getElementById('ingredientTable').children[0];

    let row = createElement('tr');

    let descriptionColumn = createElement('td');
    descriptionColumn.innerText = description;
    descriptionColumn.contentEditable = editRecipe;
    descriptionColumn.classList.add('ingredientColumn');
    descriptionColumn.classList.add('metric');

    let metricColumn = createElement('td');
    metricColumn.innerText = metric;
    metricColumn.contentEditable = editRecipe;
    metricColumn.classList.add('quantityColumn');
    metricColumn.classList.add('metric');

    let imperialColumn = createElement('td');
    imperialColumn.innerText = imperial;
    imperialColumn.contentEditable = editRecipe;
    imperialColumn.classList.add('quantityColumn');
    imperialColumn.classList.add('imperial');

    if (metric) {
        imperialColumn.style.display = "none";
    } else {
        metricColumn.style.display = "none";
    }

    let actionColumn = createElement('td');
    actionColumn.className = 'ingredientActionColumn';

    let deleteButton = createElement('span');
    deleteButton.classList.add('close');
    deleteButton.classList.add('ingredientDelete');
    deleteButton.innerText = '×';
    deleteButton.style.display = 'inline-block';

    actionColumn.appendChild(deleteButton);
    row.appendChild(descriptionColumn);
    row.appendChild(metricColumn);
    row.appendChild(imperialColumn);
    row.appendChild(actionColumn);

    ingredientTable.insertBefore(row, ingredientTable.children[ingredientTable.children.length -1]);

    addEditListener(descriptionColumn);
    addEditListener(metricColumn);
    addEditListener(imperialColumn);
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
    stepColumn.style.width = '78%';

    let actionColumn = createElement('td');
    actionColumn.className = 'ingredientActionColumn';

    let deleteButton = createElement('span');
    deleteButton.classList.add('close');
    deleteButton.classList.add('ingredientDelete');
    deleteButton.innerText = '×';
    deleteButton.style.display = 'inline-block';

    actionColumn.appendChild(deleteButton);
    row.appendChild(stepNumberColumn);
    row.appendChild(stepColumn);
    row.appendChild(actionColumn);

    stepTable.insertBefore(row, stepTable.children[stepTable.children.length -1]);

    addEditListener(stepColumn);
    addIngredientDeleteListeners();
}

function autocomplete(input, array) {
    let currentFocus;

    input.addEventListener("input", function(e) {
        let a, b, i, val = this.value;
        closeAllLists();

        if (!val) {
            return false;
        }
        currentFocus = -1;

        a = document.createElement("DIV");
        a.setAttribute("id", this.id + "autocomplete-list");
        a.setAttribute("class", "autocomplete-items");

        this.parentNode.appendChild(a);
        for (i = 0; i < array.length; i++) {
            if (array[i].substr(0, val.length).toUpperCase() === val.toUpperCase()) {
                b = document.createElement("DIV");
                b.innerHTML = "<strong>" + array[i].substr(0, val.length) + "</strong>";
                b.innerHTML += array[i].substr(val.length);
                b.innerHTML += "<input type='hidden' value='" + array[i] + "'>";
                b.addEventListener("click", function(e) {
                    input.value = this.getElementsByTagName("input")[0].value;
                    closeAllLists();
                });
                a.appendChild(b);
            }
        }
    });
    input.addEventListener("keydown", function(e) {
        var x = document.getElementById(this.id + "autocomplete-list");
        if (x) x = x.getElementsByTagName("div");
        if (e.keyCode === 40) {
            currentFocus++;
            addActive(x);
        } else if (e.keyCode === 38) {
            currentFocus--;
            addActive(x);
        } else if (e.keyCode === 13) {
            e.preventDefault();
            if (currentFocus > -1) {
                if (x) x[currentFocus].click();
            }
        }
    });
    function addActive(x) {
        if (!x) return false;
        removeActive(x);
        if (currentFocus >= x.length) currentFocus = 0;
        if (currentFocus < 0) currentFocus = (x.length - 1);
        x[currentFocus].classList.add("autocomplete-active");
    }
    function removeActive(x) {
        for (var i = 0; i < x.length; i++) {
            x[i].classList.remove("autocomplete-active");
        }
    }
    function closeAllLists(elmnt) {
        var x = document.getElementsByClassName("autocomplete-items");
        for (var i = 0; i < x.length; i++) {
            if (elmnt !== x[i] && elmnt !== input) {
                x[i].parentNode.removeChild(x[i]);
            }
        }
    }

    document.addEventListener("click", function (e) {
        closeAllLists(e.target);
    });
}
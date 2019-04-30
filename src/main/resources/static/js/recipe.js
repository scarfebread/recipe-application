let rating;

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

        if (!validateStringLength(username, 1))
        {
            showElement('invalidUsernameError');
        }

        shareRecipe(username);

        confirmShareRecipe.disabled = true;
    };

    confirmDeleteRecipe.onclick = function ()
    {
        confirmDeleteRecipe.disabled = true;

        hideElement('deleteRecipeError');

        deleteRecipe();

        confirmDeleteRecipe.disabled = true;
    };
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
        cookTime: getElementById('cookTime').innerText,
        prepTime: getElementById('prepTime').innerText,
        difficulty: difficulty.options[difficulty.selectedIndex].value
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

function deleteRecipe()
{

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
    if (event.which === 13) {
        event.preventDefault();
    }
}

function closeModal(modal)
{
    hideElement('invalidUsernameError');
    hideElement('shareRecipeError');
    hideElement('postShare');
    showElement('preShare');
    modal.style.display = "none";
    getElementById('username').value = '';
}

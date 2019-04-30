let rating;

document.addEventListener("DOMContentLoaded", function(event)
{
    rating = recipeRating;

    let modal = document.getElementById('shareRecipeModal');
    let closeModalButton = document.getElementById('closeShareRecipeModal');

    createRecipeButton.onclick = function()
    {
        modal.style.display = "block";
    };

    closeModalButton.onclick = function()
    {
        closeModal(modal);
    };

    window.onclick = function(event)
    {
        if (event.target === modal)
        {
            closeModal(modal);
        }
    };

    window.onkeydown = function(event)
    {
        if (event.key === 'Escape')
        {
            closeModal(modal);
        }
    };

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

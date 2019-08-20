DeleteRecipe = (function () {
    let deleted = false;
    let modal = null;

    let addEventListeners = function () {
        let closeDeleteRecipeModalButton = getElementById('closeDeleteRecipeModal');
        let deleteRecipeButton = getElementById('deleteRecipeButton');

        addConfirmDeleteRecipeListener();

        deleteRecipeButton.onclick = function() {
            modal.style.display = "block";
        };

        closeDeleteRecipeModalButton.onclick = function() {
            closeModal();
        };

        window.onclick = function(event) {
            if (event.target === modal) {
                closeModal();
            }
        };

        window.onkeydown = function(event) {
            if (event.key === 'Escape') {
                closeModal();
            }
        };
    };

    let addConfirmDeleteRecipeListener = function () {
        let confirmDeleteRecipe = getElementById('confirmDeleteButton');
        confirmDeleteRecipe.onclick = function () {
            confirmDeleteRecipe.disabled = true;

            hideElement('deleteRecipeError');

            deleteRecipe();

            if (!deleted) {
                confirmDeleteRecipe.disabled = false;
            }
        };
    };

    let deleteRecipe = function () {
        let recipe = {
            id: recipeId
        };

        let success = function() {
            hideElement('preDelete');
            showElement('postDelete');

            deleted = true;
        };

        let failure = function(failure) {
            getElementById('deleteRecipeError').innerText = failure;
            showElement('deleteRecipeError');
        };

        callApi("/api/recipe", HTTP_DELETE, recipe, false, success, failure);
    };

    let closeModal = function () {
        if (deleted) {
            window.location.href = '/';
        }

        modal.style.display = "none";
    };

    return {
        init: function () {
            modal = getElementById('deleteRecipeModal');
            addEventListeners();
        }
    }
})();
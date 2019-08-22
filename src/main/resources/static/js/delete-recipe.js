export const DeleteRecipe = (function () {
    let deleted = false;
    let modal;

    const addEventListeners = function () {
        const closeDeleteRecipeModalButton = getElementById('closeDeleteRecipeModal');
        const deleteRecipeButton = getElementById('deleteRecipeButton');

        addConfirmDeleteRecipeListener();

        deleteRecipeButton.onclick = function() {
            modal.style.display = "block";
        };

        closeDeleteRecipeModalButton.onclick = function() {
            closeModal();
        };
    };

    const addConfirmDeleteRecipeListener = function () {
        const confirmDeleteRecipe = getElementById('confirmDeleteButton');
        confirmDeleteRecipe.onclick = function () {
            confirmDeleteRecipe.disabled = true;

            hideElement('deleteRecipeError');

            deleteRecipe();

            if (!deleted) {
                confirmDeleteRecipe.disabled = false;
            }
        };
    };

    const deleteRecipe = function () {
        let recipe = {
            id: recipeId
        };

        const success = function() {
            hideElement('preDelete');
            showElement('postDelete');

            deleted = true;
        };

        const failure = function(failure) {
            getElementById('deleteRecipeError').innerText = failure;
            showElement('deleteRecipeError');
        };

        callApi("/api/recipe", HTTP_DELETE, recipe, false, success, failure);
    };

    const closeModal = function () {
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
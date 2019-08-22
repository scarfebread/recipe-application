export const DeleteIngredient = (function () {
    const settings = {
        url: '/api/recipe/delete-ingredient',
        deleteButton: 'ingredientDelete',
        ingredientIdAttribute: 'data-ingredientid'
    };

    const addEventListeners = function () {
        const ingredientDeleteButtons = document.getElementsByClassName(settings.deleteButton);

        Array.from(ingredientDeleteButtons).forEach(function(deleteButton) {
            addListener(deleteButton);
        });
    };

    const addListener = function(deleteButton) {
        deleteButton.addEventListener('click', function () {
            const row = deleteButton.parentNode.parentNode;
            const table = row.parentNode;

            table.removeChild(row);

            const ingredientId = row.getAttribute(settings.ingredientIdAttribute);

            deleteIngredient(ingredientId);
        });
    };

    const deleteIngredient = function (ingredientId) {
        const body = {
            ingredientId: ingredientId,
            recipeId: recipeId
        };

        const success = function() {
            EventLog.add('Deleted ingredient')
        };
        const failure = function(failure) {
            EventLog.add(`Failed to delete ingredient - ${failure}`)
        };

        callApi(settings.url, HTTP_PUT, body, false, success, failure)
    };

    return {
        init: function () {
            addEventListeners();
        },

        addListener: function(deleteButton) {
            addListener(deleteButton)
        }
    }
})();
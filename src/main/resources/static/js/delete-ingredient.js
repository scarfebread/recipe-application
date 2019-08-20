DeleteIngredient = (function () {
    let settings = {
        url: '/api/recipe/delete-ingredient',
        deleteButton: 'ingredientDelete',
        ingredientIdAttribute: 'data-ingredientid'
    };

    let addEventListeners = function () {
        let ingredientDeleteButtons = document.getElementsByClassName(settings.deleteButton);

        Array.from(ingredientDeleteButtons).forEach(function(deleteButton) {
            addListener(deleteButton);
        });
    };

    let addListener = function(deleteButton) {
        deleteButton.addEventListener('click', function () {
            let row = deleteButton.parentNode.parentNode;
            let table = row.parentNode;

            table.removeChild(row);

            let ingredientId = row.getAttribute(settings.ingredientIdAttribute);

            deleteIngredient(ingredientId);
        });
    };

    let deleteIngredient = function (ingredientId) {
        let body = {
            ingredientId: ingredientId,
            recipeId: recipeId
        };

        let success = function() {
            EventLog.add('Deleted ingredient')
        };
        let failure = function(failure) {
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
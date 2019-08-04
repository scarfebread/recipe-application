DeleteIngredient = {
    settings: {
        url: '/api/recipe/delete-ingredient',
        deleteButton: 'ingredientDelete',
        ingredientIdAttribute: 'data-ingredientid'
    },

    init: function () {
        this.bindUserActions();
    },

    bindUserActions: function () {
        let ingredientDeleteButtons = document.getElementsByClassName(this.settings.deleteButton);

        Array.from(ingredientDeleteButtons).forEach(function(deleteButton) {
            DeleteIngredient.addListener(deleteButton);
        });
    },

    addListener: function(deleteButton) {
        deleteButton.addEventListener('click', function () {
            let row = deleteButton.parentNode.parentNode;
            let table = row.parentNode;

            table.removeChild(row);

            let ingredientId = row.getAttribute(DeleteIngredient.settings.ingredientIdAttribute);

            DeleteIngredient.deleteIngredient(ingredientId);
        });
    },

    deleteIngredient: function (ingredientId) {
        let body = {
            ingredientId: ingredientId,
            recipeId: recipeId
        };

        let success = function() {};
        let failure = function() {};

        callApi(this.settings.url, HTTP_PUT, body, false, success, failure)
    }
};
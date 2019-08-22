ShoppingListIntegration = (function () {
    let bindUserActions = function () {
        let shoppingCartSymbols = document.getElementsByClassName('shoppingCartSymbol');
        Array.from(shoppingCartSymbols).forEach(function (element) {
            addEventListener(element);
        });
    };

    let addEventListener = function (element) {
        element.addEventListener('click', function () {
            if (element.classList.contains('ingredientInShoppingList')) {
                remove(element);
            } else {
                add(element);
            }
        });
    };

    let add = function (element) {
        let ingredientId = element.getAttribute('data-ingredientid');

        let success = function () {
            element.setAttribute('data-ingredientid', ingredientId);
            element.classList.add('ingredientInShoppingList');
            EventLog.add('Added ingredient to shopping list');
        };

        let failure = function (failure) {
            EventLog.add(`Failed to add to shopping list - ${failure}`);
        };

        callApi(`/api/shopping-list/add/${ingredientId}`, HTTP_POST, null, false, success, failure);
    };

    let remove = function (element) {
        let ingredientId = element.getAttribute('data-ingredientid');

        let success = function () {
            element.classList.remove('ingredientInShoppingList');
            EventLog.add('Removed ingredient from shopping list');
        };
        let failure = function (failure) {
            EventLog.add(`Unable to remove from shopping list - ${failure}`);
        };

        callApi(`/api/shopping-list/remove/${ingredientId}`, HTTP_DELETE, null, false, success, failure);
    };

    return {
        init: function () {
            bindUserActions();
        },

        addEventListener: function (element) {
            addEventListener(element);
        }
    }
})();

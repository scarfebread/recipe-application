ShoppingList = {
    init: function () {
        this.bindUserActions();
    },

    bindUserActions: function () {
        let shoppingCartSymbols = document.getElementsByClassName('shoppingCartSymbol');
        Array.from(shoppingCartSymbols).forEach(function (element) {
            ShoppingList.addEventListener(element);
        });
    },

    addEventListener: function (element) {
        element.addEventListener('click', function () {
            if (element.classList.contains('ingredientInShoppingList')) {
                ShoppingList.remove(element);
            } else {
                ShoppingList.add(element);
            }
        });
    },

    add: function (element) {
        let ingredientId = element.getAttribute('data-ingredientid');

        let success = function () {
            element.setAttribute('data-ingredientid', ingredientId);
            element.classList.add('ingredientInShoppingList');
        };

        let failure = function () {};

        callApi(`/api/shopping-list/add/${ingredientId}`, HTTP_POST, null, false, success, failure);
    },

    remove: function (element) {
        let ingredientId = element.getAttribute('data-ingredientid');

        let success = function () {
            element.classList.remove('ingredientInShoppingList');
        };
        let failure = function () {};

        callApi(`/api/shopping-list/remove/${ingredientId}`, HTTP_DELETE, null, false, success, failure);
    }
};

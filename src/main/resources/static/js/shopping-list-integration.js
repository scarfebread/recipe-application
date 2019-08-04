function addShoppingListEventListeners()
{
    let shoppingCartSymbols = document.getElementsByClassName('shoppingCartSymbol');
    Array.from(shoppingCartSymbols).forEach(function (element) {
        addShoppingListEventListener(element);
    });
}

function addShoppingListEventListener(element)
{
    element.addEventListener('click', function () {
        if (element.classList.contains('ingredientInShoppingList')) {
            removeFromShoppingList(element);
        } else {
            addToShoppingList(element);
        }
    });
}

function removeFromShoppingList(element)
{
    let ingredientId = element.getAttribute('data-ingredientid');

    let success = function () {
        element.classList.remove('ingredientInShoppingList');
    };
    let failure = function () {};

    callApi(`/api/shopping-list/remove/${ingredientId}`, HTTP_DELETE, null, false, success, failure);
}

function addToShoppingList(element)
{
    let ingredientId = element.getAttribute('data-ingredientid');

    let success = function () {
        element.setAttribute('data-ingredientid', ingredientId);
        element.classList.add('ingredientInShoppingList');
    };

    let failure = function () {};

    callApi(`/api/shopping-list/add/${ingredientId}`, HTTP_POST, null, false, success, failure);
}

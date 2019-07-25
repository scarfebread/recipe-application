document.addEventListener("DOMContentLoaded", function(event)
{
    getElementById('addShoppingListItem').onclick = function() {
        createShoppingListItem();
    };

    addCreateShoppingListItemEnterKeyEventListener(getElementById('item'));
    addCreateShoppingListItemEnterKeyEventListener(getElementById('quantity'));
});

function createShoppingListItem()
{
    // TODO inconsistent naming
    let ingredient = getValueById('item');
    let quantity = getValueById('quantity');

    if (!validateStringLength(ingredient, 1)) {
        return;
    }

    let shoppingListItem = {
        'ingredient': ingredient,
        'quantity': quantity
    };

    let success = function(data) {
        displayShoppingListItem(data)
    };

    let failure = function (error) {};

    callApi('/api/shoppingList', HTTP_POST, shoppingListItem, true, success, failure);
}

function addCreateShoppingListItemEnterKeyEventListener(element)
{
    element.addEventListener("keyup", function (event) {
        if (event.key === "Enter") {
            createShoppingListItem();
        }
    });
}

function displayShoppingListItem(item)
{
    let ingredientLabel = createElement('label');
    ingredientLabel.innerHTML = item.ingredient.description + ' ';

    let quantityLabel = createElement('label');
    quantityLabel.innerHTML = item.ingredient.metric;

    let itemAndQuantity = createElement('div');
    itemAndQuantity.className = 'itemAndQuantity';
    itemAndQuantity.appendChild(ingredientLabel);
    itemAndQuantity.appendChild(quantityLabel);

    let plusSymbol = createElement('span');
    plusSymbol.className = 'plusSymbol';
    plusSymbol.innerHTML = '+ ';

    let deleteSymbol = createElement('span');
    deleteSymbol.className = 'deleteSymbol';
    deleteSymbol.innerHTML = '&times;';

    let symbols = createElement('div');
    symbols.className = 'symbols';
    symbols.appendChild(plusSymbol);
    symbols.appendChild(deleteSymbol);

    let inventoryItem = createElement('div');
    inventoryItem.className = 'inventoryItem';
    inventoryItem.appendChild(itemAndQuantity);
    inventoryItem.appendChild(symbols);

    let inventoryContainer = getElementById('inventoryContainer');
    inventoryContainer.appendChild(inventoryItem);
}

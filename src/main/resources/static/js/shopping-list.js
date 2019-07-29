document.addEventListener("DOMContentLoaded", function(event)
{
    getElementById('addShoppingListItem').onclick = function() {
        createShoppingListItem();
    };

    addCreateShoppingListItemEnterKeyEventListener(getElementById('item'));
    addCreateShoppingListItemEnterKeyEventListener(getElementById('quantity'));

    Array.from(document.getElementsByClassName('deleteSymbol')).forEach(function (element) {
        addDeleteEventListener(element);
    });

    Array.from(document.getElementsByClassName('plusSymbol')).forEach(function (element) {
        addPurchaseEventListener(element);
    });

    // autocomplete(getElementById('item'), ingredients);
});

function addPurchaseEventListener(element)
{
    element.addEventListener('click', function () {
        let inventoryItem = element.parentNode.parentNode;
        let container = inventoryItem.parentNode;

        container.removeChild(inventoryItem);

        purchaseIngredient(inventoryItem.id);
    });
}

function addDeleteEventListener(element)
{
    element.addEventListener('click', function () {
        let inventoryItem = element.parentNode.parentNode;
        let container = inventoryItem.parentNode;

        container.removeChild(inventoryItem);

        deleteShoppingListItem(inventoryItem.id);
    });
}

function deleteShoppingListItem(id)
{
    let body = {'id': id};

    let success = function () {};
    let failure = function () {};

    callApi('/api/shopping-list', HTTP_DELETE, body, false, success, failure)
}

function purchaseIngredient(id)
{
    let body = {'id': id};

    let success = function () {};
    let failure = function () {};

    callApi('/api/shopping-list/purchase', HTTP_POST, body, false, success, failure)
}

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
        displayShoppingListItem(data);

        getElementById('item').value = '';
        getElementById('quantity').value = '';
    };

    let failure = function (error) {};

    callApi('/api/shopping-list', HTTP_POST, shoppingListItem, true, success, failure);
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
    ingredientLabel.innerHTML = item.ingredient.description;

    let itemAndQuantity = createElement('div');
    itemAndQuantity.className = 'itemAndQuantity';
    itemAndQuantity.appendChild(ingredientLabel);

    if (item.ingredient.metric) {
        ingredientLabel.className = 'insertedElement';
        let metricLabel = createElement('label');
        metricLabel.className = 'metric';
        metricLabel.innerHTML = item.ingredient.metric;

        let imperialLabel = createElement('label');
        imperialLabel.className = 'imperial';
        imperialLabel.innerHTML = item.ingredient.imperial;

        if (metric) {
            imperialLabel.style.display = 'none';
        } else {
            metricLabel.style.display = 'none';
        }

        itemAndQuantity.appendChild(metricLabel);
        itemAndQuantity.appendChild(imperialLabel);
    }

    let plusSymbol = createElement('span');
    plusSymbol.classList.add('plusSymbol');
    plusSymbol.classList.add('insertedElement');
    plusSymbol.innerHTML = '+';

    let deleteSymbol = createElement('span');
    deleteSymbol.className = 'deleteSymbol';
    deleteSymbol.innerHTML = '&times;';

    let symbols = createElement('div');
    symbols.className = 'symbols';
    symbols.appendChild(plusSymbol);
    symbols.appendChild(deleteSymbol);

    let inventoryItem = createElement('div');
    inventoryItem.className = 'inventoryItem';
    inventoryItem.id = item.id;
    inventoryItem.appendChild(itemAndQuantity);
    inventoryItem.appendChild(symbols);

    let inventoryContainer = getElementById('inventoryContainer');
    inventoryContainer.appendChild(inventoryItem);

    addDeleteEventListener(deleteSymbol);
    addPurchaseEventListener(plusSymbol);
}

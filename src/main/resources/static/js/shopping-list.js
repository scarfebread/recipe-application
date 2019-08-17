document.addEventListener("DOMContentLoaded", function(event)
{
    getElementById('addShoppingListItem').onclick = function() {
        createShoppingListItem();
    };

    addCreateShoppingListItemEnterKeyEventListener(getElementById('ingredient'));
    addCreateShoppingListItemEnterKeyEventListener(getElementById('quantity'));

    Array.from(document.getElementsByClassName('deleteSymbol')).forEach(function (element) {
        addDeleteEventListener(element);
    });

    Array.from(document.getElementsByClassName('plusSymbol')).forEach(function (element) {
        addPurchaseEventListener(element);
    });

    autocomplete(getElementById('ingredient'), ingredients);
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
    let ingredient = getValueById('ingredient');
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

        getElementById('ingredient').value = '';
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
    let template = getTemplate('shoppingListTemplate');

    let descriptionLabel = template.querySelector('.description');
    let metricLabel = template.querySelector('.metric');
    let imperialLabel = template.querySelector('.imperial');
    let deleteSymbol = template.querySelector('.deleteSymbol');
    let plusSymbol = template.querySelector('.plusSymbol');
    let inventoryItem = template.querySelector('.inventoryItem');

    descriptionLabel.innerHTML = item.ingredient.description;
    inventoryItem.id = item.id;

    if (item.ingredient.metric) {
        metricLabel.innerHTML = item.ingredient.metric;
        imperialLabel.innerHTML = item.ingredient.imperial;

        if (IngredientFormatSlider.metric) {
            imperialLabel.style.display = 'none';
        } else {
            metricLabel.style.display = 'none';
        }
    }

    let inventoryContainer = getElementById('inventoryContainer');

    inventoryContainer.appendChild(template);

    addDeleteEventListener(deleteSymbol);
    addPurchaseEventListener(plusSymbol);
}

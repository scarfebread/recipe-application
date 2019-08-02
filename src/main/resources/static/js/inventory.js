document.addEventListener("DOMContentLoaded", function(event)
{
    getElementById('addInventoryItem').onclick = function() {
        createInventoryItem();
    };

    addCreateInventoryItemEnterKeyEventListener(getElementById('item'));
    addCreateInventoryItemEnterKeyEventListener(getElementById('quantity'));

    Array.from(document.getElementsByClassName('deleteSymbol')).forEach(function (element) {
        addDeleteEventListener(element);
    });

    addShoppingListEventListeners();
    enableSearchAutoComplete();
    autocomplete(getElementById('item'), ingredients);
});

function addDeleteEventListener(element)
{
    element.addEventListener('click', function () {
        let inventoryItem = element.parentNode.parentNode;
        let container = inventoryItem.parentNode;

        container.removeChild(inventoryItem);

        deleteInventoryItem(inventoryItem.id);
    });
}

function deleteInventoryItem(id)
{
    let body = {'id': id};

    let success = function () {};
    let failure = function () {};

    callApi('/api/inventory', HTTP_DELETE, body, false, success, failure)
}

function createInventoryItem()
{
    // TODO inconsistent naming
    let ingredient = getValueById('item');
    let quantity = getValueById('quantity');

    if (!validateStringLength(ingredient, 1)) {
        return;
    }

    let inventoryItem = {
        'ingredient': ingredient,
        'quantity': quantity
    };

    let success = function(data) {
        displayInventoryItem(data);

        getElementById('item').value = '';
        getElementById('quantity').value = '';
    };

    let failure = function (error) {};

    callApi('/api/inventory', HTTP_POST, inventoryItem, true, success, failure);
}

function addCreateInventoryItemEnterKeyEventListener(element)
{
    element.addEventListener("keyup", function (event) {
        if (event.key === "Enter") {
            createInventoryItem();
        }
    });
}

function displayInventoryItem(item)
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

    let shoppingCartSymbol = createElement('i');
    shoppingCartSymbol.classList.add('fa');
    shoppingCartSymbol.classList.add('shoppingCartSymbol');
    shoppingCartSymbol.classList.add('fa-shopping-cart');
    shoppingCartSymbol.classList.add('insertedElement');
    shoppingCartSymbol.setAttribute('data-ingredientId', item.ingredient.id);

    let deleteSymbol = createElement('span');
    deleteSymbol.className = 'deleteSymbol';
    deleteSymbol.innerHTML = '&times;';

    let symbols = createElement('div');
    symbols.className = 'symbols';
    symbols.appendChild(shoppingCartSymbol);
    symbols.appendChild(deleteSymbol);

    let inventoryItem = createElement('div');
    inventoryItem.className = 'inventoryItem';
    inventoryItem.id = item.id;
    inventoryItem.appendChild(itemAndQuantity);
    inventoryItem.appendChild(symbols);

    let inventoryContainer = getElementById('inventoryContainer');
    inventoryContainer.appendChild(inventoryItem);

    addDeleteEventListener(deleteSymbol);
    addShoppingListEventListener(shoppingCartSymbol);
}

function enableSearchAutoComplete()
{
    let searchBar = getElementById('searchForIngredient');

    searchBar.addEventListener("input", function() {
        let searchText = this.value;
        let inventoryItems = document.getElementsByClassName('inventoryItem');
        Array.from(inventoryItems).forEach(function (element) {
            let description = element.querySelector("*").querySelector("*").innerHTML;

            if (description.substr(0, searchText.length).toUpperCase() === searchText.toUpperCase()) {
                element.style.display = 'block';
            } else {
                element.style.display = 'none';
            }
        });
    });
}

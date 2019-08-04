document.addEventListener("DOMContentLoaded", function(event)
{
    ShoppingList.init();
    IngredientFormatSlider.init();

    getElementById('addInventoryItem').onclick = function() {
        createInventoryItem();
    };

    addCreateInventoryItemEnterKeyEventListener(getElementById('ingredient'));
    addCreateInventoryItemEnterKeyEventListener(getElementById('quantity'));

    Array.from(document.getElementsByClassName('deleteSymbol')).forEach(function (element) {
        addDeleteEventListener(element);
    });

    enableSearchAutoComplete();
    autocomplete(getElementById('ingredient'), ingredients);
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
    let ingredient = getValueById('ingredient');
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

        getElementById('ingredient').value = '';
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
    let template = getTemplate('inventoryTemplate');

    let descriptionLabel = template.querySelector('.description');
    let metricLabel = template.querySelector('.metric');
    let imperialLabel = template.querySelector('.imperial');
    let shoppingCartSymbol = template.querySelector('.shoppingCartSymbol');
    let deleteSymbol = template.querySelector('.deleteSymbol');

    descriptionLabel.innerHTML = item.ingredient.description;
    shoppingCartSymbol.setAttribute('data-ingredientid', item.ingredient.id);

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
    ShoppingList.addEventListener(shoppingCartSymbol);
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

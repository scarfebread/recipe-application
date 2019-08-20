document.addEventListener("DOMContentLoaded", function() {
    Inventory.init();
    ShoppingListIntegration.init();
    IngredientFormatSlider.init();

    EventLog.add('Pantry loaded');
});

Inventory = (function () {
    let addEventListeners = function () {
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
    };

    let addDeleteEventListener = function (element) {
        element.addEventListener('click', function () {
            let inventoryItem = element.parentNode.parentNode;
            let container = inventoryItem.parentNode;

            container.removeChild(inventoryItem);

            deleteInventoryItem(inventoryItem.id);
        });
    };

    let deleteInventoryItem = function (id) {
        let body = {'id': id};

        let success = function () {
            EventLog.add('Ingredient deleted')
        };
        let failure = function (failure) {
            EventLog.add(`Failed to delete ingredient - ${failure}`)
        };

        callApi('/api/inventory', HTTP_DELETE, body, false, success, failure)
    };

    let createInventoryItem = function () {
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

            EventLog.add(data.ingredient.description + ' added to pantry')
        };

        let failure = function (error) {
            EventLog.add(`Failed to add ingredient to pantry - ${error}`)
        };

        callApi('/api/inventory', HTTP_POST, inventoryItem, true, success, failure);
    };

    let addCreateInventoryItemEnterKeyEventListener = function (element) {
        element.addEventListener("keyup", function (event) {
            if (event.key === "Enter") {
                createInventoryItem();
            }
        });
    };

    let displayInventoryItem = function (item) {
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
        ShoppingListIntegration.addEventListener(shoppingCartSymbol);
    };

    let enableSearchAutoComplete = function () {
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
    };

    return {
        init: function () {
            addEventListeners();
        }
    };
})();

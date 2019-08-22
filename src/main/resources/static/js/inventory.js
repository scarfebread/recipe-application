import {ShoppingListIntegration} from "./shopping-list-integration.js";
import {IngredientFormatSlider} from "./ingredient-format-slider.js";
import {EventLog} from "./event-log.js";

document.addEventListener("DOMContentLoaded", function() {
    Inventory.init();
    ShoppingListIntegration.init();
    IngredientFormatSlider.init();

    EventLog.add('Pantry loaded');
});

export const Inventory = (function () {
    const addEventListeners = function () {
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

    const addDeleteEventListener = function (element) {
        element.addEventListener('click', function () {
            const inventoryItem = element.parentNode.parentNode;
            const container = inventoryItem.parentNode;

            container.removeChild(inventoryItem);

            deleteInventoryItem(inventoryItem.id);
        });
    };

    const deleteInventoryItem = function (id) {
        const body = {'id': id};

        const success = function () {
            EventLog.add('Ingredient deleted')
        };
        const failure = function (failure) {
            EventLog.add(`Failed to delete ingredient - ${failure}`)
        };

        callApi('/api/inventory', HTTP_DELETE, body, false, success, failure)
    };

    const createInventoryItem = function () {
        const ingredient = getValueById('ingredient');
        const quantity = getValueById('quantity');

        if (!validateStringLength(ingredient, 1)) {
            return;
        }

        const inventoryItem = {
            'ingredient': ingredient,
            'quantity': quantity
        };

        const success = function(data) {
            displayInventoryItem(data);

            getElementById('ingredient').value = '';
            getElementById('quantity').value = '';

            EventLog.add(data.ingredient.description + ' added to pantry')
        };

        const failure = function (error) {
            EventLog.add(`Failed to add ingredient to pantry - ${error}`)
        };

        callApi('/api/inventory', HTTP_POST, inventoryItem, true, success, failure);
    };

    const addCreateInventoryItemEnterKeyEventListener = function (element) {
        element.addEventListener("keyup", function (event) {
            if (event.key === "Enter") {
                createInventoryItem();
            }
        });
    };

    const displayInventoryItem = function (item) {
        const template = getTemplate('inventoryTemplate');

        const inventoryItem = template.querySelector('.inventoryItem');
        const descriptionLabel = template.querySelector('.description');
        const metricLabel = template.querySelector('.metric');
        const imperialLabel = template.querySelector('.imperial');
        const shoppingCartSymbol = template.querySelector('.shoppingCartSymbol');
        const deleteSymbol = template.querySelector('.deleteSymbol');

        inventoryItem.id = item.id;
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

        const inventoryContainer = getElementById('inventoryContainer');
        inventoryContainer.appendChild(template);

        addDeleteEventListener(deleteSymbol);
        ShoppingListIntegration.addEventListener(shoppingCartSymbol);
    };

    const enableSearchAutoComplete = function () {
        const searchBar = getElementById('searchForIngredient');

        searchBar.addEventListener("input", function() {
            const searchText = this.value;
            const inventoryItems = document.getElementsByClassName('inventoryItem');
            Array.from(inventoryItems).forEach(function (element) {
                const description = element.querySelector("*").querySelector("*").innerHTML;

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

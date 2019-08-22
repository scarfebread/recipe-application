import {EventLog} from "./event-log.js";
import {IngredientFormatSlider} from "./ingredient-format-slider.js";

document.addEventListener("DOMContentLoaded", function() {
    ShoppingList.init();
    IngredientFormatSlider.init();
    EventLog.add('Shopping list loaded');
});

export const ShoppingList = (function () {
    const addEventListeners = function () {
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
    };

    const addPurchaseEventListener = function (element) {
        element.addEventListener('click', function () {
            const inventoryItem = element.parentNode.parentNode;
            const container = inventoryItem.parentNode;

            container.removeChild(inventoryItem);

            purchaseIngredient(inventoryItem.id);
        });
    };

    const addDeleteEventListener = function (element) {
        element.addEventListener('click', function () {
            const inventoryItem = element.parentNode.parentNode;
            const container = inventoryItem.parentNode;

            container.removeChild(inventoryItem);

            deleteShoppingListItem(inventoryItem.id);
        });
    };

    const deleteShoppingListItem = function (id) {
        const body = {'id': id};

        const success = function () {
            EventLog.add("Shopping list item deleted")
        };
        const failure = function (failure) {
            EventLog.add(`Failed to delete ingredient - ${failure}`)
        };

        callApi('/api/shopping-list', HTTP_DELETE, body, false, success, failure)
    };

    const purchaseIngredient = function (id) {
        const body = {'id': id};

        const success = function () {
            EventLog.add("Ingredient added to pantry")
        };
        const failure = function () {
            EventLog.add("Failed to purchase ingredient")
        };

        callApi('/api/shopping-list/purchase', HTTP_POST, body, false, success, failure)
    };

    const createShoppingListItem = function () {
        const ingredient = getValueById('ingredient');
        const quantity = getValueById('quantity');

        if (!validateStringLength(ingredient, 1)) {
            return;
        }

        const shoppingListItem = {
            'ingredient': ingredient,
            'quantity': quantity
        };

        const success = function(data) {
            displayShoppingListItem(data);

            getElementById('ingredient').value = '';
            getElementById('quantity').value = '';

            EventLog.add(data.ingredient.description + ' added to shopping list')
        };

        const failure = function (error) {
            EventLog.add(`Adding ingredient failed - ${error}`)
        };

        callApi('/api/shopping-list', HTTP_POST, shoppingListItem, true, success, failure);
    };

    const addCreateShoppingListItemEnterKeyEventListener = function (element) {
        element.addEventListener("keyup", function (event) {
            if (event.key === "Enter") {
                createShoppingListItem();
            }
        });
    };

    const displayShoppingListItem = function (item) {
        const template = getTemplate('shoppingListTemplate');

        const descriptionLabel = template.querySelector('.description');
        const metricLabel = template.querySelector('.metric');
        const imperialLabel = template.querySelector('.imperial');
        const deleteSymbol = template.querySelector('.deleteSymbol');
        const plusSymbol = template.querySelector('.plusSymbol');
        const inventoryItem = template.querySelector('.inventoryItem');

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

        const inventoryContainer = getElementById('inventoryContainer');

        inventoryContainer.appendChild(template);

        addDeleteEventListener(deleteSymbol);
        addPurchaseEventListener(plusSymbol);
    };

    return {
        init: function () {
            addEventListeners();
        }
    }
})();

import {IngredientFormatSlider} from "./ingredient-format-slider.js";
import {EventLog} from "./event-log.js";
import {ShoppingListIntegration} from "./shopping-list-integration.js";
import {DeleteIngredient} from "./delete-ingredient.js";

export const AddIngredient = (function () {
    const settings = {
        url: "/api/recipe/ingredient",
        inventoryTitleText: 'Items in your inventory:',
        inventoryDefaultText: 'Not found in inventory',
    };

    let descriptionInput;
    let quantityInput;

    const bindUserActions = function () {
        const addIngredientButton = getElementById('addIngredientButton');

        addEnterKeyEventListener(descriptionInput);
        addEnterKeyEventListener(quantityInput);

        addIngredientButton.onclick = function () {
            createIngredient();
        };
    };

    const addEnterKeyEventListener = function (element) {
        element.addEventListener("keyup", function (event) {
            if (event.key === "Enter") {
                createIngredient();
            }
        });
    };

    const createIngredient = function () {
        if (!validateStringLength(descriptionInput.value, 1)) {
            return;
        }

        const ingredient = {
            recipe: recipeId,
            description: descriptionInput.value,
            quantity: quantityInput.value
        };

        const success = function(ingredient) {
            addIngredientToPage(ingredient);
            descriptionInput.value = '';
            quantityInput.value = '';

            EventLog.add(`Ingredient ${ingredient.description} added to recipe`);
        };

        const failure = function(failure) {
            EventLog.add(`Failed to add ingredient - ${failure}`);
        };

        callApi(settings.url, HTTP_PUT, ingredient, true, success, failure);
    };

    const addIngredientToPage = function (ingredient) {
        const template = getTemplate('ingredientTemplate');

        const ingredientRow = template.querySelector('.ingredientRow');
        const descriptionColumn = template.querySelector('.ingredientColumn');
        const metricColumn = template.querySelector('.metric');
        const imperialColumn = template.querySelector('.imperial');
        const ingredientDelete = template.querySelector('.ingredientDelete');
        const shoppingCartSymbol = template.querySelector('.shoppingCartSymbol');
        const tooltipText = template.querySelector('.tooltipText');
        const inventorySymbol = template.querySelector('.inventorySymbol');

        descriptionColumn.innerText = ingredient.description;
        metricColumn.innerText = ingredient.metric;
        imperialColumn.innerText = ingredient.imperial;
        shoppingCartSymbol.setAttribute('data-ingredientid', ingredient.id);
        ingredientRow.setAttribute('data-ingredientid', ingredient.id);
        ingredientDelete.style.display = 'flex';

        if (IngredientFormatSlider.metric) {
            imperialColumn.style.display = 'none';
        } else {
            metricColumn.style.display = 'none';
        }

        if (ingredient.inventoryItems.length) {
            inventorySymbol.classList.add('ingredientInInventory');
            const inventoryItemsTitle = createParagraph();
            inventoryItemsTitle.innerText = settings.inventoryTitleText;
            tooltipText.appendChild(inventoryItemsTitle);

            Array.from(ingredient.inventoryItems).forEach(function (item) {
                const metric = createParagraph();
                metric.innerText = `${item.description} ${item.metric}`;
                metric.className = 'ingredientMetricToolTip';

                const imperial = createParagraph();
                imperial.innerText = `${item.description} ${item.imperial}`;
                imperial.className = 'ingredientImperialToolTip';

                if (IngredientFormatSlider.metric) {
                    imperial.style.display = 'none';
                } else {
                    metric.style.display = 'none';
                }

                tooltipText.appendChild(metric);
                tooltipText.appendChild(imperial);
            });
        } else {
            const inventoryItemsText = createParagraph();
            inventoryItemsText.innerText = settings.inventoryDefaultText;
            tooltipText.appendChild(inventoryItemsText);
        }

        const ingredientTable = getElementById('ingredientTable').children[0];
        ingredientTable.insertBefore(template, ingredientTable.children[ingredientTable.children.length -1]);

        DeleteIngredient.addListener(ingredientDelete);
        ShoppingListIntegration.addEventListener(shoppingCartSymbol);
    };

    const createParagraph = function () {
        return document.createElement('p');
    };

    return {
        init: function () {
            descriptionInput = getElementById('ingredientDescription');
            quantityInput = getElementById('ingredientQuantity');

            bindUserActions();
        }
    }
})();

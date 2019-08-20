AddIngredient = (function () {
    let settings = {
        url: "/api/recipe/ingredient",
        inventoryTitleText: 'Items in your inventory:',
        inventoryDefaultText: 'Not found in inventory',
    };

    let descriptionInput = null;
    let quantityInput = null;

    let bindUserActions = function () {
        let addIngredientButton = getElementById('addIngredientButton');

        addEnterKeyEventListener(descriptionInput);
        addEnterKeyEventListener(quantityInput);

        addIngredientButton.onclick = function () {
            createIngredient();
        };
    };

    let addEnterKeyEventListener = function (element) {
        element.addEventListener("keyup", function (event) {
            if (event.key === "Enter") {
                createIngredient();
            }
        });
    };

    let createIngredient = function () {
        if (!validateStringLength(descriptionInput.value, 1)) {
            return;
        }

        let ingredient = {
            recipe: recipeId,
            description: descriptionInput.value,
            quantity: quantityInput.value
        };

        let success = function(ingredient) {
            addIngredientToPage(ingredient);
            descriptionInput.value = '';
            quantityInput.value = '';

            EventLog.add(`Ingredient ${ingredient.description} added to recipe`);
        };

        let failure = function(failure) {
            EventLog.add(`Failed to add ingredient - ${failure}`);
        };

        callApi(settings.url, HTTP_PUT, ingredient, true, success, failure);
    };

    let addIngredientToPage = function (ingredient) {
        let template = getTemplate('ingredientTemplate');

        let ingredientRow = template.querySelector('.ingredientRow');
        let descriptionColumn = template.querySelector('.ingredientColumn');
        let metricColumn = template.querySelector('.metric');
        let imperialColumn = template.querySelector('.imperial');
        let ingredientDelete = template.querySelector('.ingredientDelete');
        let shoppingCartSymbol = template.querySelector('.shoppingCartSymbol');
        let tooltipText = template.querySelector('.tooltipText');
        let inventorySymbol = template.querySelector('.inventorySymbol');

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
            let inventoryItemsTitle = createParagraph();
            inventoryItemsTitle.innerText = settings.inventoryTitleText;
            tooltipText.appendChild(inventoryItemsTitle);

            Array.from(ingredient.inventoryItems).forEach(function (item) {
                let metric = createParagraph();
                metric.innerText = `${item.description} ${item.metric}`;
                metric.className = 'ingredientMetricToolTip';

                let imperial = createParagraph();
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
            let inventoryItemsText = createParagraph();
            inventoryItemsText.innerText = settings.inventoryDefaultText;
            tooltipText.appendChild(inventoryItemsText);
        }

        let ingredientTable = getElementById('ingredientTable').children[0];
        ingredientTable.insertBefore(template, ingredientTable.children[ingredientTable.children.length -1]);

        DeleteIngredient.addListener(ingredientDelete);
        ShoppingListIntegration.addEventListener(shoppingCartSymbol);
    };

    let createParagraph = function () {
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

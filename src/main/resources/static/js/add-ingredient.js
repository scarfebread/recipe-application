AddIngredient = {
    settings: {
        url: "/api/recipe/ingredient",
        inventoryTitleText: 'Items in your inventory:',
        inventoryDefaultText: 'Not found in inventory',
    },

    descriptionInput: null,
    quantityInput: null,

    init: function () {
        this.descriptionInput = getElementById('ingredientDescription');
        this.quantityInput = getElementById('ingredientQuantity');

        this.bindUserActions();
    },

    bindUserActions: function () {
        let addIngredientButton = getElementById('addIngredientButton');

        this.addEnterKeyEventListener(this.descriptionInput);
        this.addEnterKeyEventListener(this.descriptionInput);

        addIngredientButton.onclick = function () {
            AddIngredient.createIngredient();
        };
    },

    addEnterKeyEventListener: function (element) {
        element.addEventListener("keyup", function (event) {
            if (event.key === "Enter") {
                AddIngredient.createIngredient();
            }
        });
    },

    createIngredient: function () {
        if (!validateStringLength(this.descriptionInput.value, 1)) {
            return;
        }

        let ingredient = {
            recipe: recipeId,
            description: this.descriptionInput.value,
            quantity: this.quantityInput.value
        };

        let success = function(ingredient) {
            AddIngredient.addIngredientToPage(ingredient);
            AddIngredient.descriptionInput.value = '';
            AddIngredient.quantityInput.value = '';
        };

        let failure = function(failure) {};

        callApi(this.settings.url, HTTP_PUT, ingredient, true, success, failure);
    },

    addIngredientToPage: function (ingredient) {
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

        if (metric) {
            imperialColumn.style.display = 'none';
        } else {
            metricColumn.style.display = 'none';
        }

        if (ingredient.inventoryItems.length) {
            inventorySymbol.classList.add('ingredientInInventory');
            let inventoryItemsTitle = this.createParagraph();
            inventoryItemsTitle.innerText = this.settings.inventoryTitleText;
            tooltipText.appendChild(inventoryItemsTitle);

            Array.from(ingredient.inventoryItems).forEach(function (item) {
                let inventoryItem = AddIngredient.createParagraph();
                let measurement = metric ? item.metric : item.imperial;
                inventoryItem.innerText = `${item.description} ${measurement}`;
                tooltipText.appendChild(inventoryItem);
            });
        } else {
            let inventoryItemsText = this.createParagraph();
            inventoryItemsText.innerText = this.settings.inventoryDefaultText;
            tooltipText.appendChild(inventoryItemsText);
        }

        let ingredientTable = getElementById('ingredientTable').children[0];
        ingredientTable.insertBefore(template, ingredientTable.children[ingredientTable.children.length -1]);

        DeleteIngredient.addListener(ingredientDelete);
        addShoppingListEventListener(shoppingCartSymbol);
    },

    createParagraph: function () {
        return document.createElement('p');
    },
};
RecipeEditor = {
    editButton: null,
    lockButton: null,

    init: function() {
        this.editButton = getElementById('editRecipeButton');
        this.lockButton = getElementById('lockRecipeButton');

        this.bindUserActions();
    },

    bindUserActions: function () {
        this.editButton.onclick = function () {
            RecipeEditor.editRecipe();
        };

        this.lockButton.onclick = function () {
            RecipeEditor.lockRecipe();
        };
    },

    lockRecipe: function () {
        this.editButton.style.display = 'inline-block';
        this.lockButton.style.display = 'none';

        this.setDisplay('difficulty', 'none');
        this.setDisplay('difficultyLabel', 'inline-block');
        this.setDisplay('cookTime', 'none');
        this.setDisplay('prepTime', 'none');
        this.setDisplay('cookTimeLabel', 'inline-block');
        this.setDisplay('prepTimeLabel', 'inline-block');
        this.setDisplay('serves', 'none');
        this.setDisplay('servesLabel', 'inline-block');
        this.disableElement('notes');
        this.hideElement('addIngredientRow');
        this.hideElement('addStepRow');

        Array.from(document.getElementsByClassName('stepColumn')).forEach(function(element) {
            element.contentEditable = false;
            element.style.width = '95%';
            element.style.cursor = 'auto';
        });

        Array.from(document.getElementsByClassName('stepActionColumn')).forEach(function(element) {
            element.style.display = 'none';
        });

        Array.from(document.getElementsByClassName('ingredientDelete')).forEach(function(element) {
            element.style.display = 'none';
        });

        Array.from(document.getElementsByClassName('buttonColumn')).forEach(function(element) {
            element.hidden = true;
        });

        Array.from(document.getElementsByClassName('stepHeader')).forEach(function(element) {
            element.style.width = '95%';
        });
    },

    editRecipe: function () {
        this.editButton.style.display = 'none';
        this.lockButton.style.display = 'inline-block';

        this.setDisplay('difficulty', 'inline-block');
        this.setDisplay('difficultyLabel', 'none');
        this.setDisplay('cookTime', '-webkit-inline-flex');
        this.setDisplay('prepTime', '-webkit-inline-flex');
        this.setDisplay('cookTimeLabel', 'none');
        this.setDisplay('prepTimeLabel', 'none');
        this.setDisplay('serves', 'inline-block');
        this.setDisplay('servesLabel', 'none');
        this.enableElement('notes');
        this.showElement('addIngredientRow');
        this.showElement('addStepRow');

        Array.from(document.getElementsByClassName('stepColumn')).forEach(function (element) {
            element.contentEditable = true;
            element.style.width = '70%';
            element.style.cursor = 'pointer';
        });

        Array.from(document.getElementsByClassName('ingredientDelete')).forEach(function(element) {
            element.style.display = 'flex';
        });

        Array.from(document.getElementsByClassName('stepActionColumn')).forEach(function(element) {
            element.style.display = 'flex';
        });

        Array.from(document.getElementsByClassName('buttonColumn')).forEach(function (element) {
            element.hidden = false;
        });

        Array.from(document.getElementsByClassName('stepHeader')).forEach(function (element) {
            element.style.width = '65%';
        });
    },

    setDisplay: function (id, display) {
        getElementById(id).style.display = display;
    },

    hideElement: function (id) {
        getElementById(id).hidden = true;
    },

    showElement: function (id) {
        getElementById(id).hidden = false;
    },

    disableElement: function (id) {
        getElementById(id).disabled = true;
    },

    enableElement: function (id) {
        getElementById(id).disabled = false;
    },
};

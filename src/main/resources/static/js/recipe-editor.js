RecipeEditor = (function () {
    let editButton = null;
    let lockButton = null;

    let addEventListeners = function () {
        editButton.onclick = function () {
            editRecipe();
        };

        lockButton.onclick = function () {
            lockRecipe();
        };
    };

    let lockRecipe = function () {
        editButton.style.display = 'inline-block';
        lockButton.style.display = 'none';

        setDisplay('difficulty', 'none');
        setDisplay('difficultyLabel', 'inline-block');
        setDisplay('cookTime', 'none');
        setDisplay('prepTime', 'none');
        setDisplay('cookTimeLabel', 'inline-block');
        setDisplay('prepTimeLabel', 'inline-block');
        setDisplay('serves', 'none');
        setDisplay('servesLabel', 'inline-block');
        disableElement('notes');
        hideElement('addIngredientRow');
        hideElement('addStepRow');

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
    };

    let editRecipe = function () {
        editButton.style.display = 'none';
        lockButton.style.display = 'inline-block';

        setDisplay('difficulty', 'inline-block');
        setDisplay('difficultyLabel', 'none');
        setDisplay('cookTime', '-webkit-inline-flex');
        setDisplay('prepTime', '-webkit-inline-flex');
        setDisplay('cookTimeLabel', 'none');
        setDisplay('prepTimeLabel', 'none');
        setDisplay('serves', 'inline-block');
        setDisplay('servesLabel', 'none');
        enableElement('notes');
        showElement('addIngredientRow');
        showElement('addStepRow');

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
    };

    let setDisplay = function (id, display) {
        getElementById(id).style.display = display;
    };

    let hideElement = function (id) {
        getElementById(id).hidden = true;
    };

    let showElement = function (id) {
        getElementById(id).hidden = false;
    };

    let disableElement = function (id) {
        getElementById(id).disabled = true;
    };

    let enableElement = function (id) {
        getElementById(id).disabled = false;
    };
    
    return {
        init: function() {
            editButton = getElementById('editRecipeButton');
            lockButton = getElementById('lockRecipeButton');

            addEventListeners();
        }
    }
})();

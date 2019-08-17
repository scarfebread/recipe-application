AddStep  = (function () {
    let addButtonListener = function () {
        let addStepButton = getElementById('addStepButton');

        addStepButton.onclick = function () {
            createStep();
        };
    };

    let addSubmitListener = function () {
        let stepInput = getElementById('newStep');

        stepInput.addEventListener("keyup", function (event) {
            if (event.key === "Enter") {
                createStep();
            }
        });
    };

    let createStep = function () {
        let newStep = getElementById('newStep');

        if (!validateStringLength(newStep.value, 1)) {
            return;
        }

        addStepToList(newStep.value);
        updateRecipe();

        newStep.value = '';
    };

    let addStepToList = function (step) {
        let stepTable = getElementById('stepTable').children[0];

        let template = getTemplate('stepTemplate');

        let stepNumber = template.querySelector('.stepNumber');
        let stepColumn = template.querySelector('.stepColumn');
        let stepActionColumn = template.querySelector('.stepActionColumn');
        let stepDelete = template.querySelector('.stepDelete');

        stepNumber.innerText = (getSteps().length + 1) + '.';
        stepColumn.innerText = step;
        stepActionColumn.style.display = 'flex';

        stepTable.insertBefore(template, stepTable.children[stepTable.children.length -1]);

        addEditListener(stepColumn);
        addDeleteListener(stepDelete);
    };

    let addEditListener = function (step) {
        step.addEventListener("keydown", function (event) {
            if (event.key === "Enter") {
                event.preventDefault();
                step.blur();
            }
        });

        step.addEventListener('blur', function () {
            if (!validateStringLength(step.innerHTML, 1)) {
                let row = step.parentNode;
                let table = row.parentNode;

                table.removeChild(row);
            }

            updateRecipe();
        });
    };

    let addEditListeners = function () {
        let steps = document.getElementsByClassName('stepColumn');
        Array.from(steps).forEach(function(element) {
            addEditListener(element);
        });
    };

    let addDeleteListener = function (deleteButton) {
        deleteButton.addEventListener('click', function () {
            let row = deleteButton.parentNode.parentNode;
            let table = row.parentNode;

            table.removeChild(row);

            updateRecipe();
        });
    };

    let addDeleteListeners = function () {
        let steps = document.getElementsByClassName('stepDelete');
        Array.from(steps).forEach(function(element) {
            addDeleteListener(element);
        });
    };

    return {
        init: function () {
            addButtonListener();
            addSubmitListener();
            addEditListeners();
            addDeleteListeners();
        }
    }
})();

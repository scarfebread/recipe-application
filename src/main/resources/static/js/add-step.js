AddStep = (function () {
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

        let body = {
            description: newStep.value,
            recipe: recipeId
        };

        let success = function (step) {
            addStepToList(step.id, step.description);
            newStep.value = '';

            EventLog.add('Added step to recipe')
        };

        let failure = function (failure) {
            EventLog.add(`Failed to add step to recipe - ${failure}`)
        };

        callApi('/api/recipe/add-step', HTTP_PUT, body, true, success, failure);
    };

    let addStepToList = function (stepId, description) {
        let stepTable = getElementById('stepTable').children[0];

        let template = getTemplate('stepTemplate');

        let stepNumber = template.querySelector('.stepNumber');
        let stepColumn = template.querySelector('.stepColumn');
        let stepRow = template.querySelector('.stepRow');
        let stepActionColumn = template.querySelector('.stepActionColumn');
        let stepDelete = template.querySelector('.stepDelete');

        stepNumber.innerText = getNextStepNumber() + '.';
        stepColumn.innerText = description;
        stepActionColumn.style.display = 'flex';
        stepRow.setAttribute('data-stepid', stepId);

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
            let stepRow = step.parentNode;

            if (!validateStringLength(step.innerHTML, 1)) {
                deleteStep(stepRow);
                return;
            }

            let body = {
                id: stepRow.getAttribute('data-stepid'),
                description : step.innerHTML,
                recipe: recipeId
            };

            let success = function () {
                EventLog.add('Step updated')
            };
            let failure = function (failure) {
                EventLog.add(`Failed to update step - ${failure}`)
            };

            callApi('/api/recipe/update-step', HTTP_PUT, body, false, success, failure);
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
            deleteStep(
                deleteButton.parentNode.parentNode
            );
        });
    };

    let deleteStep = function (stepRow) {
        let table = stepRow.parentNode;

        let body = {
            stepId: stepRow.getAttribute('data-stepid'),
            recipeId: recipeId
        };

        let success = function () {
            table.removeChild(stepRow);
            updateStepNumbers();

            EventLog.add('Step deleted')
        };

        let failure = function (failure) {
            EventLog.add(`Failed to remove step - ${failure}`)
        };

        callApi('/api/recipe/delete-step', HTTP_PUT, body, false, success, failure);
    };

    let addDeleteListeners = function () {
        let steps = document.getElementsByClassName('stepDelete');
        Array.from(steps).forEach(function(element) {
            addDeleteListener(element);
        });
    };

    let getNextStepNumber = function () {
        let steps = document.querySelectorAll('.stepColumn');

        return steps.length + 1;
    };

    let updateStepNumbers = function () {
        let stepNumber = 1;
        let stepNumbers = document.querySelectorAll('.stepNumber');

        Array.from(stepNumbers).forEach(function (step) {
            step.innerHTML = stepNumber + '.';
            stepNumber++;
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

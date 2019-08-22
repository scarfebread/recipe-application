export const AddStep = (function () {
    const addButtonListener = function () {
        const addStepButton = getElementById('addStepButton');

        addStepButton.onclick = function () {
            createStep();
        };
    };

    const addSubmitListener = function () {
        const stepInput = getElementById('newStep');

        stepInput.addEventListener("keyup", function (event) {
            if (event.key === "Enter") {
                createStep();
            }
        });
    };

    const createStep = function () {
        const newStep = getElementById('newStep');

        if (!validateStringLength(newStep.value, 1)) {
            return;
        }

        const body = {
            description: newStep.value,
            recipe: recipeId
        };

        const success = function (step) {
            addStepToList(step.id, step.description);
            newStep.value = '';

            EventLog.add('Added step to recipe')
        };

        const failure = function (failure) {
            EventLog.add(`Failed to add step to recipe - ${failure}`)
        };

        callApi('/api/recipe/add-step', HTTP_PUT, body, true, success, failure);
    };

    const addStepToList = function (stepId, description) {
        const stepTable = getElementById('stepTable').children[0];

        const template = getTemplate('stepTemplate');

        const stepNumber = template.querySelector('.stepNumber');
        const stepColumn = template.querySelector('.stepColumn');
        const stepRow = template.querySelector('.stepRow');
        const stepActionColumn = template.querySelector('.stepActionColumn');
        const stepDelete = template.querySelector('.stepDelete');

        stepNumber.innerText = getNextStepNumber() + '.';
        stepColumn.innerText = description;
        stepActionColumn.style.display = 'flex';
        stepRow.setAttribute('data-stepid', stepId);

        stepTable.insertBefore(template, stepTable.children[stepTable.children.length -1]);

        addEditListener(stepColumn);
        addDeleteListener(stepDelete);
    };

    const addEditListener = function (step) {
        step.addEventListener("keydown", function (event) {
            if (event.key === "Enter") {
                event.preventDefault();
                step.blur();
            }
        });

        step.addEventListener('blur', function () {
            const stepRow = step.parentNode;

            if (!validateStringLength(step.innerHTML, 1)) {
                deleteStep(stepRow);
                return;
            }

            const body = {
                id: stepRow.getAttribute('data-stepid'),
                description : step.innerHTML,
                recipe: recipeId
            };

            const success = function () {
                EventLog.add('Step updated')
            };
            const failure = function (failure) {
                EventLog.add(`Failed to update step - ${failure}`)
            };

            callApi('/api/recipe/update-step', HTTP_PUT, body, false, success, failure);
        });
    };

    const addEditListeners = function () {
        const steps = document.getElementsByClassName('stepColumn');
        Array.from(steps).forEach(function(element) {
            addEditListener(element);
        });
    };

    const addDeleteListener = function (deleteButton) {
        deleteButton.addEventListener('click', function () {
            deleteStep(
                deleteButton.parentNode.parentNode
            );
        });
    };

    const deleteStep = function (stepRow) {
        const table = stepRow.parentNode;

        const body = {
            stepId: stepRow.getAttribute('data-stepid'),
            recipeId: recipeId
        };

        const success = function () {
            table.removeChild(stepRow);
            updateStepNumbers();

            EventLog.add('Step deconsted')
        };

        const failure = function (failure) {
            EventLog.add(`Failed to remove step - ${failure}`)
        };

        callApi('/api/recipe/delete-step', HTTP_PUT, body, false, success, failure);
    };

    const addDeleteListeners = function () {
        const steps = document.getElementsByClassName('stepDelete');
        Array.from(steps).forEach(function(element) {
            addDeleteListener(element);
        });
    };

    const getNextStepNumber = function () {
        const steps = document.querySelectorAll('.stepColumn');

        return steps.length + 1;
    };

    const updateStepNumbers = function () {
        let stepNumber = 1;
        const stepNumbers = document.querySelectorAll('.stepNumber');

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

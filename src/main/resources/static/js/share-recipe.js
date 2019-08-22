export const ShareRecipe = (function () {
    let enabled = true;
    let modal;

    const addEventListeners = function () {
        const shareRecipeButton = getElementById('shareRecipeButton');
        const confirmShareRecipe = getElementById('confirmShareButton');
        const closeShareRecipeModalButton = getElementById('closeShareRecipeModal');
        const input = getElementById('username');

        confirmShareRecipe.onclick = function () {
            submit();
        };

        input.addEventListener("keydown", function (event) {
            if (event.key === "Enter") {
                submit();
            }
        });

        shareRecipeButton.onclick = function() {
            modal.style.display = "block";
        };

        closeShareRecipeModalButton.onclick = function() {
            closeModal();
        };
    };

    const submit = function () {
        hideElement('invalidUsernameError');
        hideElement('shareRecipeError');

        const username = getValueById('username');

        if (validateStringLength(username, 1)) {
            share(username);
        } else {
            showElement('invalidUsernameError');
        }
    };

    const share = function (newUser) {
        if (!enabled) {
            return;
        }

        enabled = false;

        const recipe = {
            id: recipeId,
            newUser: newUser
        };

        const success = function() {
            hideElement('preShare');
            showElement('postShare');
        };

        const failure = function(failure) {
            enabled = true;
            getElementById('shareRecipeError').innerText = failure;
            showElement('shareRecipeError');
        };

        callApi("/api/recipe/share", HTTP_POST, recipe, false, success, failure);
    };

    const closeModal = function () {
        hideElement('invalidUsernameError');
        hideElement('shareRecipeError');
        hideElement('postShare');
        showElement('preShare');
        modal.style.display = "none";
        getElementById('username').value = '';
    };

    return {
        init: function () {
            modal = getElementById('shareRecipeModal');
            addEventListeners();
        }
    }
})();
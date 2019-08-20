ShareRecipe = (function () {
    let enabled = true;
    let modal = null;

    let addEventListeners = function () {
        let shareRecipeButton = getElementById('shareRecipeButton');
        let confirmShareRecipe = getElementById('confirmShareButton');
        let closeShareRecipeModalButton = getElementById('closeShareRecipeModal');
        let input = getElementById('username');

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

        window.onclick = function(event) {
            if (event.target === modal) {
                closeModal();
            }
        };

        window.onkeydown = function(event) {
            if (event.key === 'Escape') {
                closeModal();
            }
        };
    };

    let submit = function () {
        hideElement('invalidUsernameError');
        hideElement('shareRecipeError');

        let username = getValueById('username');

        if (validateStringLength(username, 1)) {
            share(username);
        } else {
            showElement('invalidUsernameError');
        }
    };

    let share = function (newUser) {
        if (!enabled) {
            return;
        }

        enabled = false;

        let recipe = {
            id: recipeId,
            newUser: newUser
        };

        let success = function() {
            hideElement('preShare');
            showElement('postShare');
        };

        let failure = function(failure) {
            enabled = true;
            getElementById('shareRecipeError').innerText = failure;
            showElement('shareRecipeError');
        };

        callApi("/api/recipe/share", HTTP_POST, recipe, false, success, failure);
    };

    let closeModal = function () {
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
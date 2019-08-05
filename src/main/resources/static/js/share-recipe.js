ShareRecipe = {
    enabled: true,

    init: function () {
        this.bindUserActions();
    },

    bindUserActions: function () {
        let confirmShareRecipe = getElementById('confirmShareButton');
        let input = getElementById('username');

        confirmShareRecipe.onclick = function () {
            ShareRecipe.submit();
        };

        input.addEventListener("keydown", function (event) {
            if (event.key === "Enter") {
                ShareRecipe.submit();
            }
        });
    },

    submit: function () {
        hideElement('invalidUsernameError');
        hideElement('shareRecipeError');

        let username = getValueById('username');

        if (validateStringLength(username, 1)) {
            ShareRecipe.share(username);
        } else {
            showElement('invalidUsernameError');
        }
    },

    share: function (newUser) {
        if (!this.enabled) {
            return;
        }

        this.enabled = false;

        let recipe = {
            id: recipeId,
            newUser: newUser
        };

        let success = function() {
            hideElement('preShare');
            showElement('postShare');
        };

        let failure = function(failure) {
            ShareRecipe.enabled = true;
            getElementById('shareRecipeError').innerText = failure;
            showElement('shareRecipeError');
        };

        callApi("/api/recipe/share", HTTP_POST, recipe, false, success, failure);
    }
};
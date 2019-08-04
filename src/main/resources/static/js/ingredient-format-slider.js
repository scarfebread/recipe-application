IngredientFormatSlider = {
    metric: true,

    init: function () {
        this.bindUserActions();
    },

    bindUserActions: function () {
        let ingredientFormatButton = getElementById('ingredientFormatButton');

        ingredientFormatButton.onclick = function() {
            this.metric = !this.metric;

            let displayClass;
            let hideClass;

            if (this.metric) {
                displayClass = 'metric';
                hideClass = 'imperial';
            } else {
                displayClass = 'imperial';
                hideClass = 'metric';
            }

            let displayProperty = null;

            Array.from(document.getElementsByClassName(hideClass)).forEach(function(element) {
                if (!displayProperty) {
                    displayProperty = getComputedStyle(element, null).display;
                }

                element.style.display = "none";
            });

            Array.from(document.getElementsByClassName(displayClass)).forEach(function(element) {
                element.style.display = displayProperty;
            });
        };
    }
};
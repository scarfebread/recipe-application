export const IngredientFormatSlider = (function () {
    const addEventListeners = function () {
        const ingredientFormatButton = getElementById('ingredientFormatButton');

        ingredientFormatButton.onclick = function() {
            IngredientFormatSlider.metric = !IngredientFormatSlider.metric;

            let displayClass;
            let hideClass;
            let ingredientToolTipDisplayClass;
            let ingredientToolTipHideClass;

            if (IngredientFormatSlider.metric) {
                displayClass = 'metric';
                hideClass = 'imperial';
                ingredientToolTipDisplayClass = 'ingredientMetricToolTip';
                ingredientToolTipHideClass = 'ingredientImperialToolTip';
            } else {
                displayClass = 'imperial';
                hideClass = 'metric';
                ingredientToolTipHideClass = 'ingredientMetricToolTip';
                ingredientToolTipDisplayClass = 'ingredientImperialToolTip';
            }

            let displayProperty;

            Array.from(document.getElementsByClassName(hideClass)).forEach(function(element) {
                if (!displayProperty) {
                    displayProperty = getComputedStyle(element, null).display;
                }

                element.style.display = "none";
            });

            Array.from(document.getElementsByClassName(displayClass)).forEach(function(element) {
                element.style.display = displayProperty;
            });

            Array.from(document.getElementsByClassName(ingredientToolTipHideClass)).forEach(function(element) {
                element.style.display = "none";
            });

            Array.from(document.getElementsByClassName(ingredientToolTipDisplayClass)).forEach(function(element) {
                element.style.display = 'block';
            });
        };
    };

    return {
        metric: true,

        init: function () {
            addEventListeners();
        }
    }
})();
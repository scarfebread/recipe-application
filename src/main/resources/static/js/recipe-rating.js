import {Recipe} from "./recipe.js";

export const RecipeRating = (function () {
    const addEventListeners = function () {
        const rating1 = getElementById('rating1');
        const rating2 = getElementById('rating2');
        const rating3 = getElementById('rating3');
        const rating4 = getElementById('rating4');
        const rating5 = getElementById('rating5');

        rating1.onclick = function () {
            recipeRating = 1;
            displayRating();
            Recipe.update();
        };

        rating2.onclick = function () {
            recipeRating = 2;
            displayRating();
            Recipe.update();
        };

        rating3.onclick = function () {
            recipeRating = 3;
            displayRating();
            Recipe.update()
        };

        rating4.onclick = function () {
            recipeRating = 4;
            displayRating();
            Recipe.update()
        };

        rating5.onclick = function () {
            recipeRating = 5;
            displayRating();
            Recipe.update()
        };
    };

    const displayRating = function () {
        const starRating = getElementById('starRating');

        const children = starRating.children;
        for (let i = 0; i < children.length; i++) {
            let star = children[i];

            star.classList.remove('checked');

            if (i < recipeRating) {
                star.classList.add('checked');
            }
        }
    };

    return {
        init: function () {
            displayRating();
            addEventListeners();
        }
    }
})();
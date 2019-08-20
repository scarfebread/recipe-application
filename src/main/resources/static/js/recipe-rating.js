RecipeRating = (function () {
    let addEventListeners = function () {
        let rating1 = getElementById('rating1');
        let rating2 = getElementById('rating2');
        let rating3 = getElementById('rating3');
        let rating4 = getElementById('rating4');
        let rating5 = getElementById('rating5');

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

    let displayRating = function () {
        let starRating = getElementById('starRating');

        let children = starRating.children;
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
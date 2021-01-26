import React, {useState} from 'react';
import CreateRecipe from "./CreateRecipe";
import RecipeSummary from "./RecipeSummary";

const Home = () => {
    const [recipes, setRecipes] = useState(preLoadedRecipes);

    const addRecipe = (recipe) => {
        recipes.push(recipe);
        setRecipes(recipes);
    }

    return (
        <>
            <div className="createAndSearch">
                <CreateRecipe recipes={recipes} addRecipe={addRecipe} />
            </div>
            <div>
                <div>
                    <h2>Your recipes:</h2>
                </div>
                <div>
                    {recipes.map(recipe =>
                        <RecipeSummary recipe={recipe} />
                    )}
                </div>
            </div>
        </>
    );
}

export default Home;
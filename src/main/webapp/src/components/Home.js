import React, {useState} from 'react';
import CreateRecipe from "./CreateRecipe";

const Home = () => {
    const [recipes, setRecipes] = useState([]);

    const addRecipe = (recipe) => {
        recipes.push(recipe);
        setRecipes(recipes);
    }

    return (
        <div className="createAndSearch">
            <CreateRecipe recipes={recipes} addRecipe={addRecipe} />
        </div>
    );
}

export default Home;
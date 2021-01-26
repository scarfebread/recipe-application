import React from 'react';

const RecipeSummary = (props) => {
    const recipe = props.recipe;

    const getStars = () => {
        const stars = [];

        for (let i = 1; i <= 5; i++) {
            if (recipe.rating >= i) {
                stars.push(<span key={i} className="fa fa-star star checked"/>)
            } else {
                stars.push(<span className="fa fa-star star"/>)
            }
        }

        return stars;
    }

    return (
        <div className="recipeListItem">
            <a className="recipeLink" href={"/recipe?id=" + recipe.id}>
                <h3 className="recipeTitle">{recipe.title}</h3>
            </a>
            <label className="totalTime">Total time: {recipe.totalTime}</label>
            <br/>
            {getStars()}
        </div>
    );
}

export default RecipeSummary;

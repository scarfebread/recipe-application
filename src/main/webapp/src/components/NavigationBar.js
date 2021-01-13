import React from 'react';

const NavigationBar = () => (
    <div className="navbar">
        <a href="/">Home</a>
        <div className="dropdown">
            <button className="dropbtn">Recipes
                <i className="fa fa-caret-down caretDown"/>
            </button>
            <div id="recipeDropdown" className="dropdown-content">
                <label>Recent recipes:</label>
                {
                    recentlyViewedRecipes.map((recentlyViewedRecipe) => (
                        <a href={'/recipe?id=' + recentlyViewedRecipe.recipe.id}>
                            {recentlyViewedRecipe.recipe.title}
                        </a>
                    ))
                }
            </div>
        </div>
        <a href="/inventory">Pantry</a>
        <a href="/shopping-list">Shopping List</a>
        <div className="dropdown active">
            <button className="dropbtn active">Account
                <i className="fa fa-caret-down caretDown"/>
            </button>
            <div className="dropdown-content">
                <a href="/logout">Logout</a>
                <a href="/change-password">Change password</a>
                <a href="/delete-account">Delete account</a>
            </div>
        </div>
    </div>
);

export default NavigationBar;

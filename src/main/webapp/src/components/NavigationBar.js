import React from 'react';

const NavigationBar = (props) => (
    <div className="navbar">
        <a href="/" className={props.activeMenu === 'home' ? 'active' : ''}>
            Home
        </a>
        <div className={props.activeMenu === 'recipes' ? 'dropdown active' : 'dropdown'}>
            <button className="dropbtn">Recipes
                <i className="fa fa-caret-down caretDown"/>
            </button>
            <div className="dropdown-content">
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
        <a href="/inventory" className={props.activeMenu === 'pantry' ? 'active' : ''}>
            Pantry</a>
        <a href="/shopping-list" className={props.activeMenu === 'shoppingList' ? 'active' : ''}>
            Shopping List
        </a>
        <div className={props.activeMenu === 'account' ? 'dropdown active' : 'dropdown'}>
            <button className={props.activeMenu === 'account' ? 'dropbtn active' : 'dropbtn'}>Account
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

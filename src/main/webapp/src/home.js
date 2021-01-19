import React from 'react';
import ReactDOM from 'react-dom';
import Header from "./components/Header";
import CreateRecipe from "./components/CreateRecipe";

ReactDOM.hydrate(
    <div className="baseContainer container">
        <div>
            <Header/>
            <CreateRecipe/>
        </div>
    </div>,
    document.getElementById('mountNode'),
);
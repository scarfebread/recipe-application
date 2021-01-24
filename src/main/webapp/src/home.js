import React from 'react';
import ReactDOM from 'react-dom';
import Header from "./components/Header";
import NavigationBar from "./components/NavigationBar";
import Home from "./components/Home";

ReactDOM.hydrate(
    <div className="baseContainer container">
        <div>
            <Header/>
            <NavigationBar activeMenu={"home"}/>
            <Home/>
        </div>
    </div>,
    document.getElementById('mountNode'),
);
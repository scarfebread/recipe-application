import React from 'react';
import ReactDOM from 'react-dom';
import Header from "./components/Header";
import ChangePassword from "./components/ChangePassword";
import NavigationBar from "./components/NavigationBar";

ReactDOM.hydrate(
    <div className="container">
        <div>
            <Header/>
            <NavigationBar activeMenu={"account"}/>
            <ChangePassword/>
        </div>
    </div>,
    document.getElementById('mountNode'),
);
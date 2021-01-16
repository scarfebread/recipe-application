import React from 'react';
import ReactDOM from 'react-dom';
import Header from "./components/Header";
import ChangePassword from "./components/ChangePassword";

ReactDOM.hydrate(
    <div className="container">
        <div>
            <Header/>
            <ChangePassword/>
        </div>
    </div>,
    document.getElementById('mountNode'),
);
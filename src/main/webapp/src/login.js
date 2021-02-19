import React from 'react';
import ReactDOM from 'react-dom';
import LoginForm from "./components/LoginForm";

ReactDOM.hydrate(
    <div className="container">
        <div>
            <LoginForm/>
        </div>
    </div>,
    document.getElementById('mountNode'),
);
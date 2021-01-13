import React, { useState } from 'react';
import Header from "./Header";
import ChangePassword from "./ChangePassword";

const App = () => (
    <div className="container">
        <div>
            <Header/>
            <ChangePassword/>
        </div>
    </div>
);

export default App;
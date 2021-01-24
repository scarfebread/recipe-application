import React from "react";

const Header = () => (
    <div>
        <h1 className="title">The Cooking Pot</h1>
        <div className="loggedInUserContainer">
            <div className="loggedInUser">
                <label>{loggedInUser}</label>
                <a href="/logout" style={{paddingLeft: 7}}>
                    Logout
                </a>
            </div>
        </div>
    </div>
);

export default Header;

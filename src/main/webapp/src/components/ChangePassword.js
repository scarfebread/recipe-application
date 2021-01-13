import React, {useState} from 'react';
import ApiClient from "./ApiClient";

const ChangePassword = () => {
    const [form, setForm] = useState({password: '', retypepassword: ''});
    const [error, setError] = useState();

    const returnKeyEventHandler = (event) => {
        if (event.key === "Enter") {
            changePassword();
        }
    }

    const changePassword = () => {
        if (!validate()) {
            return false;
        }

        ApiClient.
    }

    const validate = () => {
        setError(null);

        if (form.password !== form.retypepassword) {
            setError('Passwords do not match!');
            return false;
        }

        if (form.password.length < 5) {
            setError('Password must be more than 5 characters!');
            return false;
        }
    }

    const renderError = () => {
        if (error) {
            return <label className="error">{error}</label>;
        }
    }

    return (
        <div className="form">
            <label className="pageTitle">Change your password</label>
            <div id="preChangePasswordDisplay">
                <label>New password</label><br/>
                <input
                    type="password"
                    onKeyUp={returnKeyEventHandler}
                    onChange={event => setForm(form => ({...form, password: event.target.value}))}
                    value={form.password}
                />
                <br/>
                <label>Retype password</label><br/>
                <input
                    type="password"
                    onKeyUp={returnKeyEventHandler}
                    onChange={event => setForm(form => ({...form, retypepassword: event.target.value}))}
                    value={form.retypepassword}
                />
                {renderError()}
                <button className="button" onClick={changePassword}>
                    <span>CHANGE PASSWORD</span>
                </button>
            </div>
            <div id="postChangePasswordDisplay" className="hidden">
                <label>Password successfully changed!</label><br/>
            </div>
        </div>
    );
}

export default ChangePassword;

import React, {useState} from 'react';
import ApiClient from "./ApiClient";

const ChangePassword = () => {
    const [form, setForm] = useState({password: '', retypepassword: ''});
    const [error, setError] = useState();
    const [successful, setSuccessful] = useState(false);
    let submitted = false;

    const returnKeyEventHandler = (event) => {
        if (event.key === "Enter") {
            changePassword();
        }
    }

    const changePassword = () => {
        if (submitted) {
            return false;
        }

        submitted = true;

        if (!validate()) {
            return false;
        }

        const success = () => {
            setSuccessful(true);
        }

        const failure = (error) => {
            setError(error);
            submitted = false;
        }

        ApiClient.post('/api/change-password', {password: form.password}, success, failure);
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

        return true;
    }

    const renderError = () => {
        if (error) {
            return <label className="error">{error}</label>;
        }
    }

    const renderForm = () => (
        <div>
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
    );

    const renderSuccess = () => {
        const styles = {
            paddingTop: 10,
            width: '100%',
            float: 'left',
            textAlign: 'center'
        }

        return (
            <label style={styles}>Password successfully changed!</label>
        );
    }

    return (
        <div className="form">
            <label className="pageTitle">Change your password</label>
            {successful ?  renderSuccess() : renderForm()}
        </div>
    );
}

export default ChangePassword;

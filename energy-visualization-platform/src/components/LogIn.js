import React, {useState} from "react";
import {Link, Outlet} from "react-router-dom";
import 'bootstrap/dist/css/bootstrap.min.css';
import "./LogIn.css"
import axios from "axios";

function LogIn(){

    const [errorMessages, setErrorMessages] = useState({});
    const [adminIsSubmitted, setAdminIsSubmitted] = useState(false);
    const [clientIsSubmitted, setClientIsSubmitted] = useState(false);
    const [userRegistration, setUserRegistration] = useState({
        username: "",
        password: ""
    });

    const errors = {
        uname: "invalid username",
        pass: "invalid password",
        noType: "error! system does not know the user's type",
    };

    const handleInput = (event) => {
        const name = event.target.name;
        const value = event.target.value;
        setUserRegistration({ ...userRegistration, [name] : value});
    }

    const handleSubmit = (event) => {
        event.preventDefault();

        axios
            .get("http://localhost:8080/person/login", {
                params: {
                    username: userRegistration.username,
                    password: userRegistration.password
                }
            })
            .then((response) => {
                if (response.data === "username_error") {
                    setErrorMessages({name: "uname", message: errors.uname});
                    localStorage.removeItem("user");
                } else if (response.data === "password_error"){
                    setErrorMessages({name: "pass", message: errors.pass});
                    localStorage.removeItem("user");
                } else if(response.data === "admin"){
                    setAdminIsSubmitted(true);
                    localStorage.setItem("user", JSON.stringify(userRegistration));
                } else if(response.data === "client"){
                    setClientIsSubmitted(true);
                    localStorage.setItem("user", JSON.stringify(userRegistration));
                } else {
                    setErrorMessages({name: "noType", message: errors.noType});
                    localStorage.removeItem("user");
                }
            })
            .catch((error) =>
                console.error("There was an error!", error.response.data.message)
            );
    };

    const renderErrorMessage = (name) =>
        name === errorMessages.name && (
            <div className="error">{errorMessages.message}</div>
        );

    const renderForm = (
        <form onSubmit = {handleSubmit} className="form-user">
            <h3>Sign In</h3>
            <div>
                <div className="input-container-col">
                    <label>Username: </label>
                    <input type="text"
                           value={userRegistration.username}
                           onChange={handleInput}
                           name="username" required id = "username"/>
                </div>
                {renderErrorMessage("uname")}
                <div className="input-container-col">
                    <label>Password: </label>
                    <input type="password"
                           value={userRegistration.password}
                           onChange={handleInput}
                           name="password" required id = "password"/>
                </div>
                {renderErrorMessage("pass")}
            </div>

            <div className="button-container">
                <input type="submit"/>
                <Link to="/">
                    <button className="go-back">Go back</button>
                </Link>
            </div>
        </form>
    );

    const renderAdminConfirmation = (
        <div>
            <h4>Admin has successfully logged in!</h4>
            <div className="button-container">
                <Link to="/AdminActions">
                    <button className="log-in-button">Go to admin page</button>
                </Link>
            </div>

        </div>
    );

    const renderClientConfirmation = (
        <div>
            <h4>Client has successfully logged in!</h4>
            <div className="button-container">
            <Link to="/ClientActions">
                <button className="log-in-button">Go to client page</button>
            </Link>
            </div>
        </div>
    );

    const render = () => {
        if(adminIsSubmitted){
            return renderAdminConfirmation;
        }else if(clientIsSubmitted){
            return renderClientConfirmation;
        }else{
            return renderForm;
        }
    }

    return (
        <div className="app">
            <div className="login-form">
                {render()}
                <Outlet />
            </div>
        </div>
    );
}

export default LogIn;
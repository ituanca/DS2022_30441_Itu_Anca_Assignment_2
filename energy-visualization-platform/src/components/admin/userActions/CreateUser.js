import React, {useState} from "react";
import {Link, Outlet} from "react-router-dom";
import axios from "axios";
import "./CreateUser.css";
import validator from "validator/es";

function CreateUser(){

    const [errorMessages, setErrorMessages] = useState({});
    const [isSubmitted, setIsSubmitted] = useState(false);
    const [user, setUser] = useState({
        name: "",
        username: "",
        password: "",
        address: "",
        age: "",
        type: ""
    });
    const [selectedType, setSelectedType] = useState("");

    const errors = {
        name: "invalid name",
        username: "username already exists",
        password: "password should have minimum length 8, at least 1 lowercase, 1 uppercase, 1 number, 1 symbol",
        age: "invalid age",
        under18: "user must be 18 years old",
        type: "type was not selected"
    };

    const renderErrorMessage = (name) =>
        name === errorMessages.name && (
            <div className="error">{errorMessages.message}</div>
        );

    const validate = () => {
        console.log(user);
        if(!validator.isStrongPassword(user.password, {minLength: 8, minLowercase: 1, minUppercase: 1, minNumbers: 1, minSymbols: 1})) {
            setErrorMessages({name: "password", message: errors.password});
        } else if(user.age < 0 || (!Number.isInteger(parseFloat(user.age)))) {
            setErrorMessages({name: "age", message: errors.age});
        }else if(user.age < 18) {
            setErrorMessages({name: "under18", message: errors.under18});
        } else if(user.type === ""){
            setErrorMessages({name: "type", message: errors.type});
        } else {
            return true;
        }
        return false;
    }

    const handleSubmit = (event) => {
        event.preventDefault();
        if(validate()){
            axios
                .post("http://localhost:8080/person/", user)
                .then((response) => {
                    if(response.data === "username_error") {
                        setErrorMessages({name: "username", message: errors.username});
                    }else{
                        setIsSubmitted(true);
                    }
                })
                .catch((error) => {
                    console.error("There was an error!", error.response.data.message)
                });
        }
    };

    const handleInput = event => {
        const name = event.target.name;
        const value = event.target.value;
        setUser({ ...user, [name] : value});
    }

    const handleChange = e => {
        const target = e.target;
        if (target.checked) {
            setSelectedType(target.value);
        }
        setUser({ ...user, type: target.value});
    };

    const renderForm = (
        <form onSubmit = {handleSubmit} className="form-user">
            <div>
                Insert details of the new user:
                <div>
                        <div className="input-container-row">
                            <label>Name: </label>
                            <input type="text"
                                   value={user.name}
                                   onChange={handleInput}
                                   name="name" required
                                   id = "name"/>
                        </div>
                        <div className="input-container-row">
                            <label>Username: </label>
                            <input type="text"
                                   value={user.username}
                                   onChange={handleInput}
                                   name="username" required
                                   id="username"/>
                        </div>
                        {renderErrorMessage("username")}
                        <div className="input-container-row">
                            <label>Password: </label>
                            <input type="password"
                                   value={user.password}
                                   onChange={handleInput}
                                   name="password" required
                                   id = "password"/>
                        </div>
                        {renderErrorMessage("password")}
                        <div className="input-container-row">
                            <label>Address: </label>
                            <input type="text"
                                   value={user.address}
                                   onChange={handleInput}
                                   name="address" required
                                   id="address"/>
                        </div>
                        <div className="input-container-row">
                            <label>Age: </label>
                            <input type="number"
                                   value={user.age}
                                   onChange={handleInput}
                                   name="age" required
                                   id="age"/>
                        </div>
                        {renderErrorMessage("age")}
                        {renderErrorMessage("under18")}
                        <div className="input-container-row">
                            <label>Role: </label>
                            <div className="radio-button">
                                <input
                                    type="radio"
                                    name="role"
                                    value="admin"
                                    checked={selectedType === "admin"}
                                    onChange={handleChange}
                                />
                                <label>Admin</label>
                            </div>
                            <div className="radio-button">
                                <input
                                    type="radio"
                                    name="role"
                                    value="client"
                                    checked={selectedType === "client"}
                                    onChange={handleChange}
                                />
                                <label>Client</label>
                            </div>
                        </div>
                        {renderErrorMessage("type")}
                    </div>
            </div>
            <div className="button-container">
                <input type="submit"/>
            </div>
        </form>
    );

    return (
        <div className="app">
            <div className="login-form">
                <h3>Create user</h3>
                {isSubmitted ? <h5>User was created successfully!</h5> : renderForm}
                <nav>
                    <Link to="/AdminActions">
                        <button className="go-back">Go back</button>
                    </Link>
                </nav>
                <Outlet />
            </div>
        </div>
    );
}

export default CreateUser;
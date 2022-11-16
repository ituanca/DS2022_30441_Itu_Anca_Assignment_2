import React, {useEffect, useState} from "react";
import {Link} from "react-router-dom";
import axios from "axios";
import "./UpdateUser.css";

function UpdateUser(){

    const [errorMessages, setErrorMessages] = useState({});
    const [isSubmitted, setIsSubmitted] = useState(false);
    const [isSelected, setIsSelected] = useState(false);
    const [user, setUser] = useState({
        id: "",
        name: "",
        username: "",
        address: "",
        age: "",
        type: ""
    });
    const [users, setUsers] = useState( [] );
    const [selectedType, setSelectedType] = useState("");

    useEffect(() => {
        fetch('http://localhost:8080/person')
            .then((response) => response.json())
            .then((json) => {
                setUsers(json);
            })
            .catch((error) => {
                console.log(error);
            });
    }, []);

    const errors = {
        username: "invalid username",
        age: "invalid age",
        under18: "user must be 18 years old",
        type: "type was not selected",
        error: "an error occurred, the user cannot be updated"
    };

    const renderErrorMessage = (name) =>
        name === errorMessages.name && (
            <div className="error">{errorMessages.message}</div>
        );

    const validate = () => {
        console.log(user);
        if(user.age < 0 || (!Number.isInteger(parseFloat(user.age)))) {
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
        console.log(user);
        if(validate()){
            axios
                .put("http://localhost:8080/person/", user)
                .then((response) => {
                    if (response.data === "username_error") {
                        setErrorMessages({name: "username", message: errors.username});
                    } else if(response.data === "error"){
                        setErrorMessages({name: "error", message: errors.error});
                    } else{
                        setIsSubmitted(true);
                    }
                })
                .catch((error) => {
                    console.error("There was an error!", error.response.data.message)
                });
        }
    };

    const handleInput = (event) => {
        const name = event.target.name;
        const value = event.target.value;
        setUser({ ...user, [name] : value});
    }

    const handleRadioButton = (event) => {
        const target = event.target;
        if (target.checked) {
            setSelectedType(target.value);
        }
        setUser({ ...user, type: target.value});
    }

    const renderFormWithDetails = (
        <form onSubmit = {handleSubmit} className="form-user">
            <div>
                Update details of the user:
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
                            onChange={handleRadioButton}
                        />
                        <label>Admin</label>
                    </div>
                    <div className="radio-button">
                        <input
                            type="radio"
                            name="role"
                            value="client"
                            checked={selectedType === "client"}
                            onChange={handleRadioButton}
                        />
                        <label>Client</label>
                    </div>
                </div>
                {renderErrorMessage("type")}
                {renderErrorMessage("error")}
            </div>
            <div className="button-container">
                <input type="submit"/>
            </div>
        </form>
    );

    const renderComboBox = (
        <div>
            <form className="form-user">
                <div className="input-container-row">
                    <label>Select a user:</label>
                    <select className="select-user" onChange={event => {
                        for(let i = 0; i < users.length; i++){
                            if (event.target.value === users.at(i).username){
                                setUser(users.at(i));
                                setSelectedType(users.at(i).type);
                            }
                        }
                        setIsSelected(true);
                    }}>
                        {users.map(({id, username}) => (
                            <option key={id} value={username}>
                                {username}
                            </option>
                        ))}
                    </select>
                </div>
            </form>
            {isSelected ? renderFormWithDetails : null}
        </div>
    );

    const renderUpdateConfirmed = (
        <div>
            <h5>User successfully updated!</h5>
            <span>&nbsp;&nbsp;</span>
        </div>
    );

    const render = () => {
        if(isSubmitted){
            return renderUpdateConfirmed;
        }else{
            return renderComboBox;
        }
    }

    return (
        <div className="app">
            <div className="login-form">
                <div className="login-form-update-user">
                    <h3>Update user</h3>
                    <span>&nbsp;&nbsp;</span>
                    {render()}
                    <nav>
                        <Link to="/AdminActions">
                            <button className="go-back">Go back</button>
                        </Link>
                    </nav>
                </div>
            </div>
        </div>
    );
}

export default UpdateUser;
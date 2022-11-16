import React, {useEffect, useState} from "react";
import {Link} from "react-router-dom";
import axios from "axios";
import "./DeleteUser.css";

function DeleteUser(){

    const [errorMessages, setErrorMessages] = useState({});
    const [isDeleted, setIsDeleted] = useState(false);
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
        error: "an error occurred, the user cannot be updated"
    };

    const renderErrorMessage = (name) =>
        name === errorMessages.name && (
            <div className="error">{errorMessages.message}</div>
        );

    const handleSubmit = (event) => {
        event.preventDefault();
        axios
            .delete("http://localhost:8080/person/" +  user.username)
            .then((response) => {
                if(response.data === "error"){
                    setErrorMessages({name: "error", message: errors.error});
                } else{
                    setIsDeleted(true);
                }
            })
            .catch((error) => {
                console.error("There was an error!", error.response.data.message)
            });
    };

    const renderFormWithDetails = (
        <form onSubmit = {handleSubmit} className="form-user">
            <div className="input-container-col">
                <label>Name: <strong>{user.name}</strong> </label>
            </div>
            <div className="input-container-col">
                <label>Username: <strong>{user.username}</strong></label>
            </div>
            <div className="input-container-col">
                <label>Address: <strong>{user.address}</strong></label>
            </div>
            <div className="input-container-col">
                <label>Age: <strong>{user.age}</strong></label>
            </div>
            <div className="input-container-col">
                <label>Role: <strong>{user.type}</strong></label>
            </div>
            {renderErrorMessage("error")}
            <div className="button-container">
                <button type="submit">Delete user</button>
            </div>
        </form>
    );

    const renderForm = (
        <div>
            <form className="form-user">
                <div className="input-container-col">
                    <label>Select a user:</label>
                    <select className="select-user" onChange={event => {
                        for(let i = 0; i < users.length; i++){
                            if (event.target.value === users.at(i).username){
                                setUser(users.at(i));
                                setIsSelected(true);
                            }
                        }
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

    const renderDeletionConfirmed = (
        <div>
            <span>&nbsp;&nbsp;</span>
            <h5>User successfully deleted!</h5>
            <span>&nbsp;&nbsp;</span>
        </div>
    );

    const render = () => {
        if(isDeleted){
            return renderDeletionConfirmed;
        }else{
            return renderForm;
        }
    }

    return (
        <div className="app">
            <div className="login-form">
                <div className="login-form-delete-user">
                    <h3>Delete user</h3>
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

export default DeleteUser;
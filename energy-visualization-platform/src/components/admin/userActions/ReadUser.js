import React, {useEffect, useState} from "react";
import {Link, Outlet} from "react-router-dom";
import "./ReadUser.css"

function ReadUser(){

    const [isSelected, setIsSelected] = useState(false);
    const [user, setUser] = useState({
        name: "",
        username: "",
        address: "",
        age: "",
        type: "",
        ownedDevices: {}
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

    const renderFormWithComboBox = (
        <form>
            <div className="input-container-col">
                <label>Select a user:</label>
                <select className="select-user" onChange={event => {
                    for(let i = 0; i < users.length; i++){
                        if (event.target.value === users.at(i).username){
                            setUser(users.at(i))
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
    );

    const renderDisplayedOwnedDevices = (
        <div>
            <label> Owned devices: </label>
            {user.ownedDevices!== null && user.ownedDevices.length > 0
                ?
                <div>
                    {user.ownedDevices.map(({id, name, address}) => (
                        <li key={id} value={name}>
                            <strong>{name}</strong>, address: {address}
                        </li>
                    ))}
                </div>
                :
                <strong className="no-owner-text"> there are no devices associated to this owner</strong>
            }
        </div>
    );

    const renderFormWithInfo = (
        <div>
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
            <div className="input-container-col">
                {user.type === "client" ? renderDisplayedOwnedDevices : null}
            </div>
        </div>
    );

    const render = ()  => (
        <form className="form-user">
            {renderFormWithComboBox}
            {isSelected ? renderFormWithInfo : null}
        </form>
    );

    return (
        <div className="app">
            <div className="login-form">
                <h3>View users</h3>
                <span>&nbsp;&nbsp;</span>
                {render()}
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

export default ReadUser;
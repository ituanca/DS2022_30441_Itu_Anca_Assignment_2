import React, {useEffect, useState} from "react";
import {Link, Outlet} from "react-router-dom";
import "./EditMappings.css"
import axios from "axios";

function EditMappings(){

    const [errorMessages, setErrorMessages] = useState({});
    const [isSubmitted, setIsSubmitted] = useState(false);
    const [, setDeviceIsSelected] = useState(false);
    const [, setUserIsSelected] = useState(false);
    const [device, setDevice] = useState({
        name: "",
        description: "",
        address: "",
        maxHourlyEnergyConsumption: "",
        owner: []
    });
    const [devices, setDevices] = useState( [] );
    const [user, setUser] = useState({
        name: "",
        username: "",
        address: "",
        age: "",
        type: ""
    });
    const [users, setUsers] = useState( [] );

    const errors = {
        error: "an error occurred, the mapping cannot be updated"
    };

    const renderErrorMessage = (name) =>
        name === errorMessages.name && (
            <div className="error">{errorMessages.message}</div>
        );

    useEffect(() => {
        fetch('http://localhost:8080/device/withOwner')
            .then((response) => response.json())
            .then((json) => {
                setDevices(json);
                setDevice(json[0]);
                setUser(json[0].owner)
            })
            .catch((error) => {
                console.log(error);
            });
    }, []);

    useEffect(() => {
        fetch('http://localhost:8080/person/clients')
            .then((response) => response.json())
            .then((json) => {
                setUsers(json);
            })
            .catch((error) => {
                console.log(error);
            });
    }, []);

    const handleSubmit = (event) => {
        event.preventDefault();
        let data = [device.name, user.username];
        axios
            .put("http://localhost:8080/device/updateAssociation", data)
            .then((response) => {
                if(response.data === "error") {
                    setErrorMessages({name: "error", message: errors.error});
                }else{
                    setIsSubmitted(true);
                }
            })
            .catch((error) => {
                console.error("There was an error!", error.response.data.message)
            });
    };

    const renderDeviceFormWithInfo = (
        <div>
            <div className="input-container-col">
                <label> Name: <strong>{device.name}</strong> </label>
            </div>
            <div className="input-container-col">
                <label> Description: <strong>{device.description}</strong> </label>
            </div>
            <div className="input-container-col">
                <label> Address: <strong>{device.address}</strong></label>
            </div>
            <div className="input-container-col">
                <label> Maximum hourly energy consumption(Watt):
                    <strong> {device.maxHourlyEnergyConsumption} Watt</strong>
                </label>
            </div>
            <div className="input-container-col">
                <label> Owner:
                    {device.owner != null
                        ? <strong> {device.owner.username}</strong>
                        : <strong className="no-owner-text"> no owner associated with the device</strong>}
                </label>
            </div>
        </div>
    );

    const renderUserFormWithInfo = (
        <div>
            <h5>Owner:</h5>
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
        </div>
    );

    const renderForm = (
        <form onSubmit = {handleSubmit}>
            <div className="container-multiple-cols">
                <div className="form-container-device">
                    <h5>Choose a device:</h5>
                    <div className="input-container-col">
                        <select placeholder="" className="select-device-mini-form" onChange={event => {
                            for(let i = 0; i < devices.length; i++){
                                if (event.target.value === devices.at(i).name){
                                    setDevice(devices.at(i))
                                    setUser(devices.at(i).owner)
                                }
                            }
                            setDeviceIsSelected(true);
                        }}>
                            {devices.map(({id, name}) => (
                                <option key={id} value={name}>
                                    {name}
                                </option>
                            ))}
                        </select>
                    </div>
                    {renderDeviceFormWithInfo}
                    {renderErrorMessage("device")}
                </div>
                <div className="invisible-container">
                    <div className="mini-form-container">
                        {renderUserFormWithInfo}
                    </div>
                    <div className="mini-form-container">
                        <h5>Change the owner:</h5>
                        <div className="input-container-col">
                            <select className="select-user-mini-form" onChange={event => {
                                for(let i = 0; i < users.length; i++){
                                    if (event.target.value === users.at(i).username){
                                        setUser(users.at(i))
                                    }
                                }
                                setUserIsSelected(true);
                            }}>
                                {users.map(({id, username}) => (
                                    <option key={id} value={username}>
                                        {username}
                                    </option>
                                ))}
                            </select>
                        </div>
                    </div>
                </div>
            </div>
            <button type="submit">Update mapping</button>
        </form>
    );

    const renderMappingDeletionConfirmed = (
        <div>
            <span>&nbsp;&nbsp;</span>
            <h5>Device <strong>{device.name}</strong> is now owned by <strong>{user.username}</strong>.</h5>
            <span>&nbsp;&nbsp;</span>
        </div>
    );

    const render = () => {
        if(isSubmitted){
            return renderMappingDeletionConfirmed;
        }else{
            return renderForm;
        }
    }

    return (
        <div className="app">
            <div className="login-form-mappings">
                <h3>Edit mappings between devices and clients</h3>
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

export default EditMappings;
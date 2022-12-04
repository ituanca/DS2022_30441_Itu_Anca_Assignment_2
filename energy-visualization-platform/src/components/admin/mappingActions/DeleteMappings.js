import React, {useEffect, useState} from "react";
import {Link, Outlet} from "react-router-dom";
import "./DeleteMappings.css"
import axios from "axios";

function DeleteMappings(){

    const [errorMessages, setErrorMessages] = useState({});
    const [isSubmitted, setIsSubmitted] = useState(false);
    const [, setDeviceIsSelected] = useState(false);
    const [device, setDevice] = useState({
        id: "",
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

    const errors = {
        device: "device not selected",
        user: "user not selected",
        error: "an error occurred, the mapping cannot be done"
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
            })
            .catch((error) => {
                console.log(error);
            });
    }, []);

    const handleSubmit = (event) => {
        event.preventDefault();
        setUser(device.owner);
        axios
            .put("http://localhost:8080/device/deleteAssociation", device)
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
        </div>
    );

    const renderUserFormWithInfo = (
        <div>
            <div className="input-container-col">
                <label>Name: <strong>{device.owner.name}</strong> </label>
            </div>
            <div className="input-container-col">
                <label>Username: <strong>{device.owner.username}</strong></label>
            </div>
            <div className="input-container-col">
                <label>Address: <strong>{device.owner.address}</strong></label>
            </div>
            <div className="input-container-col">
                <label>Age: <strong>{device.owner.age}</strong></label>
            </div>
            <div className="input-container-col">
                <label>Role: <strong>{device.owner.type}</strong></label>
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
                </div>
                <div className="form-container-user">
                    <h5>Owner of the selected device:</h5>
                    <div className="input-container-col">
                        {renderUserFormWithInfo}
                    </div>
                </div>
            </div>
            {renderErrorMessage("error")}
            <button type="submit">Delete mapping</button>
        </form>
    );

    const renderMappingDeletionConfirmed = (
        <div>
            <span>&nbsp;&nbsp;</span>
            <h5>Device <strong>{device.name}</strong> is not owned by <strong>{user.username}</strong> anymore.</h5>
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
                <h3>Delete mappings between devices and clients</h3>
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

export default DeleteMappings;
import React, {useEffect, useState} from "react";
import {Link} from "react-router-dom";
import axios from "axios";
import "./DeleteDevice.css";

function DeleteDevice(){

    const [errorMessages, setErrorMessages] = useState({});
    const [isDeleted, setIsDeleted] = useState(false);
    const [isSelected, setIsSelected] = useState(false);
    const [device, setDevice] = useState({
        name: "",
        description: "",
        address: "",
        maxHourlyEnergyConsumption: "",
        owner: {}
    });
    const [devices, setDevices] = useState( [] );

    useEffect(() => {
        fetch('http://localhost:8080/device')
            .then((response) => response.json())
            .then((json) => {
                setDevices(json);
            })
            .catch((error) => {
                console.log(error);
            });
    }, []);

    const errors = {
        error: "an error occurred, the device cannot be deleted"
    };

    const renderErrorMessage = (name) =>
        name === errorMessages.name && (
            <div className="error">{errorMessages.message}</div>
        );

    const handleSubmit = (event) => {
        event.preventDefault();
        axios
            .delete("http://localhost:8080/device/" +  device.name)
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
        <form onSubmit = {handleSubmit}>
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
                            ? <strong> {device.owner.username} ({device.owner.name})</strong>
                            : <strong className="no-owner-text"> no owner associated with the device</strong>}
                    </label>
                </div>
                {renderErrorMessage("error")}
            </div>
            <div className="button-container">
                <button type="submit">Delete device</button>
            </div>
        </form>
    );

    const renderForm = (
        <div>
            <div className="input-container-col">
                <label>Select a device:</label>
                <select className="select-device" onChange={event => {
                    for(let i = 0; i < devices.length; i++){
                        if (event.target.value === devices.at(i).name){
                            setDevice(devices.at(i));
                            setIsSelected(true);
                        }
                    }
                }}>
                    {devices.map(({id, name}) => (
                        <option key={id} value={name}>
                            {name}
                        </option>
                    ))}
                </select>
            </div>
            {isSelected ? renderFormWithDetails : null}
        </div>
    );

    const renderDeletionConfirmed = (
        <div>
            <span>&nbsp;&nbsp;</span>
            <h5>Device successfully deleted!</h5>
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
                <div className="login-form-delete-device">
                    <h3>Delete device</h3>
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

export default DeleteDevice;
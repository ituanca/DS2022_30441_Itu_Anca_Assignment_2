import React, {useEffect, useState} from "react";
import {Link} from "react-router-dom";
import axios from "axios";
import "./UpdateDevice.css";

function UpdateDevice(){

    const [errorMessages, setErrorMessages] = useState({});
    const [isSubmitted, setIsSubmitted] = useState(false);
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
        name: "name already exists",
        description: "description is too long",
        maxHourlyEnergyConsumption: "invalid quantity",
        error: "an error occurred, the device cannot be updated"
    };

    const renderErrorMessage = (name) =>
        name === errorMessages.name && (
            <div className="error">{errorMessages.message}</div>
        );

    const validate = () => {
        if(device.description.length > 500){
            setErrorMessages({name: "description", message: errors.description});
        } else if(device.maxHourlyEnergyConsumption < 0) {
            setErrorMessages({name: "maxHourlyEnergyConsumption", message: errors.maxHourlyEnergyConsumption});
        } else {
            return true;
        }
        return false;
    }

    const handleSubmit = (event) => {
        event.preventDefault();
        if(validate()){
            axios
                .put("http://localhost:8080/device/", device)
                .then((response) => {
                    if(response.data === "name_error"){
                        setErrorMessages({name: "name", message: errors.name})
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
        setDevice({ ...device, [name] : value});
    }

    const renderFormWithDetails = (
        <form onSubmit = {handleSubmit}>
            <div>
                Update details of the device:
                <div className="input-container-row">
                    <label>Name: </label>
                    <input type="text"
                           value={device.name}
                           onChange={handleInput}
                           name="name" required
                           id = "name"/>
                </div>
                {renderErrorMessage("name")}
                <div className="input-container-row">
                    <label>Description: </label>
                    <textarea
                           value={device.description}
                           onChange={handleInput}
                           name="description" required
                           id="description"
                           rows="6"/>
                </div>
                {renderErrorMessage("description")}
                <div className="input-container-row">
                    <label>Address: </label>
                    <input type="text"
                           value={device.address}
                           onChange={handleInput}
                           name="address" required
                           id="address"/>
                </div>
                <div className="input-container-row">
                    <label>Maximum hourly energy consumption(Watt): </label>
                    <input type="number"
                           value={device.maxHourlyEnergyConsumption}
                           onChange={handleInput}
                           name="maxHourlyEnergyConsumption" required
                           id="maxHourlyEnergyConsumption"/>
                </div>
                {renderErrorMessage("maxHourlyEnergyConsumption")}
                {renderErrorMessage("error")}
            </div>
            <div className="button-container">
                <input type="submit"/>
            </div>
        </form>
    );

    const renderForm = (
        <div>
            <div className="input-container-row">
                <label>Select a device:</label>
                <select className="select-device" onChange={event => {
                    for(let i = 0; i < devices.length; i++){
                        if (event.target.value === devices.at(i).name){
                            setDevice(devices.at(i));
                        }
                    }
                    setIsSelected(true);
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

    const renderUpdateConfirmed = (
        <div>
            <h5>Device successfully updated!</h5>
            <span>&nbsp;&nbsp;</span>
        </div>
    );

    const render = () => {
        if(isSubmitted){
            return renderUpdateConfirmed;
        }else{
            return renderForm;
        }
    }

    return (
        <div className="app">
            <div className="login-form">
                <div className="login-form-update-device">
                    <h3>Update device</h3>
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

export default UpdateDevice;
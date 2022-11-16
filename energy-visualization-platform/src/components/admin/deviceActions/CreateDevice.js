import React, {useState} from "react";
import {Link} from "react-router-dom";
import axios from "axios";
import "./CreateDevice.css";

function CreateDevice(){

    const [errorMessages, setErrorMessages] = useState({});
    const [isSubmitted, setIsSubmitted] = useState(false);
    const [device, setDevice] = useState({
        name: "",
        description: "",
        address: "",
        maxHourlyEnergyConsumption: "",
        owner: {}
    });

    const errors = {
        name: "name already exists",
        description: "description is too long",
        maxHourlyEnergyConsumption: "invalid quantity",
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
        console.log(device);
        event.preventDefault();
        if(validate()){
            axios
                .post("http://localhost:8080/device/", device)
                .then((response) => {
                    if(response.data === "name_error"){
                        setErrorMessages({name: "name", message: errors.name})
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
        setDevice({ ...device, [name] : value});
    }

    const renderForm = (
        <form onSubmit = {handleSubmit}>
            <div>
                Insert details of the new device:
                <div>
                    <div className="input-container-col">
                        <label>Name: </label>
                        <input type="text"
                               value={device.name}
                               onChange={handleInput}
                               name="name" required
                               id = "name"/>
                    </div>
                    {renderErrorMessage("name")}
                    <div className="input-container-col">
                        <label>Description: </label>
                        <textarea
                               value={device.description}
                               onChange={handleInput}
                               name="description" required
                               id = "description"
                               rows="6"/>
                    </div>
                    {renderErrorMessage("description")}
                    <div className="input-container-col">
                        <label>Address: </label>
                        <input type="text"
                               value={device.address}
                               onChange={handleInput}
                               name="address" required
                               id="address"/>
                    </div>
                    <div className="input-container-col">
                        <label>Maximum hourly energy consumption(Watt): </label>
                        <input type="number"
                               value={device.maxHourlyEnergyConsumption}
                               onChange={handleInput}
                               name="maxHourlyEnergyConsumption" required
                               id="maxHourlyEnergyConsumption"/>
                    </div>
                    {renderErrorMessage("maxHourlyEnergyConsumption")}
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
                <div className="login-form-create-device">
                    <h3>Add device</h3>
                    {isSubmitted ? <h5>Device was added successfully!</h5> : renderForm}
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

export default CreateDevice;
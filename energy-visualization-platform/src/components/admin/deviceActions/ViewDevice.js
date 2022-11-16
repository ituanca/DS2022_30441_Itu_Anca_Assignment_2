import React, {useEffect, useState} from "react";
import {Link, Outlet} from "react-router-dom";
import "./ViewDevice.css"

function ViewDevice(){

    const [isSubmitted, setIsSubmitted] = useState(false);
    const [device, setDevice] = useState({
        name: "",
        description: "",
        address: "",
        maxHourlyEnergyConsumption: "",
        owner: []
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

    console.log(devices)

    const renderFormWithComboBox = (
        <form>
            <div className="input-container-col">
                <label>Select a device:</label>
                <select className="select-device" onChange={event => {
                    for(let i = 0; i < devices.length; i++){
                        if (event.target.value === devices.at(i).name){
                            setDevice(devices.at(i))
                        }
                    }
                    setIsSubmitted(true);
                }}>
                    {devices.map(({id, name}) => (
                        <option key={id} value={name}>
                            {name}
                        </option>
                    ))}
                </select>
            </div>
        </form>
    );

    const renderFormWithInfo = (
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
        </div>
    );

    return (
        <div className="app">
            <div className="login-form">
                <h3>View devices</h3>
                <span>&nbsp;&nbsp;</span>
                {renderFormWithComboBox}
                {isSubmitted ? renderFormWithInfo : null}
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

export default ViewDevice;
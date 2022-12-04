import React, {useEffect, useState} from "react";
import {Link, Outlet} from "react-router-dom";
import "./ViewDevices.css"
import axios from "axios";

function ViewDevices(){

    const [, setIsSubmitted] = useState(false);
    const [device, setDevice] = useState({
        name: "",
        description: "",
        address: "",
        maxHourlyEnergyConsumption: "",
        owner: [],
        hourlyEnergyConsumption: {}
    });
    const [user, setUser] = useState(JSON.parse(localStorage.getItem("user")));
    const [devices, setDevices] = useState( [] );

    useEffect(() => {
        setUser(JSON.parse(localStorage.getItem("user")));
        axios
            .get('http://localhost:8080/person/ownedDevicesWithoutHourlyConsumption', {
                params: {
                    username: user.username
                }
            })
            .then((response) => {
                if(response.data!==null && response.data.length > 0){
                    setDevices(response.data);
                    setDevice(response.data[0])
                }
            })
            .catch((error) => {
                console.log(error);
            });
    }, []);

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

    const render = () => {
        if(devices!==null && devices.length > 0){
            return (
                <div>
                    {renderFormWithComboBox}
                    {renderFormWithInfo}
                </div>
            );
        }else{
            return (
                <h4 className="h4-no-devices">You do not own any devices</h4>
            );
        }
    };

    return (
        <div className="app">
            <div className="login-form">
                <h3>View your devices</h3>
                <span>&nbsp;&nbsp;</span>
                {render()}
                <nav>
                    <Link to="/ClientActions">
                        <button className="go-back">Go back</button>
                    </Link>
                </nav>
                <Outlet />
            </div>
        </div>
    );
}

export default ViewDevices;
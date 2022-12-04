import React, {useEffect, useState} from "react";
import {Link, Outlet} from "react-router-dom";
import "./EditMappings.css"

function ViewMappings(){

    const [, setDeviceIsSelected] = useState(false);
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
    const [, setUsers] = useState( [] );

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
            <h5>Owner of the selected device:</h5>
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
        <form>
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
                </div>
                <div className="form-container-user">
                    {renderUserFormWithInfo}
                </div>
            </div>
        </form>
    );

    return (
        <div className="app">
            <div className="login-form-mappings">
                <h3>View mappings between devices and clients</h3>
                {renderForm}
                <div className="button-container">
                </div>
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

export default ViewMappings;
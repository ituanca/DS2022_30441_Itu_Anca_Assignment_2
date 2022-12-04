import React from "react";
import {Link} from "react-router-dom";
import "./AdminActions.css"

function AdminActions(){

    return (
        <div className="app">
            <div className="login-form">
                <h3>Admin</h3>
                <span>&nbsp;&nbsp;</span>
                <div className="container-multiple-cols">
                    <div className="button-container-admin-actions">
                        <h4>Users</h4>
                        <div>
                            <Link to="/CreateUser">
                                <button className="users-button">Create user</button>
                            </Link>
                        </div>
                        <span>&nbsp;&nbsp;</span>
                        <div>
                            <Link to="/ViewUsers">
                                <button className="users-button">View users</button>
                            </Link>
                        </div>
                        <span>&nbsp;&nbsp;</span>
                        <div>
                            <Link to="/EditUsers">
                                <button className="users-button">Edit users</button>
                            </Link>
                        </div>
                        <span>&nbsp;&nbsp;</span>
                        <div>
                            <Link to="/DeleteUsers">
                                <button className="users-button">Delete users</button>
                            </Link>
                        </div>
                    </div>
                    <div className="button-container-admin-actions">
                        <h4>Devices</h4>
                        <div>
                            <Link to="/CreateDevice">
                                <button className="device-button">Create device</button>
                            </Link>
                        </div>
                        <span>&nbsp;&nbsp;</span>
                        <div>
                            <Link to="/ViewDevice">
                                <button className="device-button">View devices</button>
                            </Link>
                        </div>
                        <span>&nbsp;&nbsp;</span>
                        <div>
                            <Link to="/EditDevices">
                                <button className="device-button">Edit devices</button>
                            </Link>
                        </div>
                        <span>&nbsp;&nbsp;</span>
                        <div>
                            <Link to="/DeleteDevices">
                                <button className="device-button">Delete devices</button>
                            </Link>
                        </div>
                    </div>
                    <div className="button-container-admin-actions">
                        <h4>Mappings</h4>
                        <div>
                            <Link to="/CreateMappings">
                                <button className="mappings-button">Map devices to clients</button>
                            </Link>
                        </div>
                        <span>&nbsp;&nbsp;</span>
                        <div>
                            <Link to="/ViewMappings">
                                <button className="mappings-button">View mappings</button>
                            </Link>
                        </div>
                        <span>&nbsp;&nbsp;</span>
                        <div>
                            <Link to="/EditMappings">
                                <button className="mappings-button">Edit mappings</button>
                            </Link>
                        </div>
                        <span>&nbsp;&nbsp;</span>
                        <div>
                            <Link to="/DeleteMappings">
                                <button className="mappings-button">Delete mappings</button>
                            </Link>
                        </div>
                    </div>
                </div>
                <span>&nbsp;&nbsp;</span>
                <div>
                    <Link to="/Login">
                        <button className="go-back">Go back</button>
                    </Link>
                </div>
            </div>
        </div>
    );
}

export default AdminActions;
import {Link} from "react-router-dom";
import Popup from "../webSocketConnection/Popup";
import React, {useEffect, useState} from "react";
import Connect from "../webSocketConnection/Connection";

function ClientActions(){

    const [popupTriggered, setPopupTriggered] = useState(false);

    useEffect(() => {
        setTimeout(() => {
            setPopupTriggered(true);
        }, 3000);
    }, []);

    return (
        <div className="app">
            <div className="login-form">
                <h3>Client</h3>
                <Popup trigger={popupTriggered} setTrigger={setPopupTriggered}>
                    <h3>One of your devices overcame its threshold!</h3>
                    <Connect></Connect>
                </Popup>
                <div className="button-container">
                    <div>
                        <Link to="/ViewDevices">
                            <button className="users-button">View your devices</button>
                        </Link>
                    </div>
                    <span>&nbsp;&nbsp;</span>
                    <div>
                        <Link to="/ViewDailyEnergyConsumption">
                            <button className="users-button">View the daily energy consumption of your devices</button>
                        </Link>
                    </div>
                    <span>&nbsp;&nbsp;</span>
                    <div>
                        <Link to="/LogIn">
                            <button className="go-back">Go back</button>
                        </Link>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default ClientActions;
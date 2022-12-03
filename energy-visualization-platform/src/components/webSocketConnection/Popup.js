import React from "react";
import './Popup.css'
import Connect from "./Connection";

function Popup(props){
    return( props.trigger) ? (
        <div className="popup">
            <div className="popup-inner">
                <button className="close-btn" onClick={() => props.setTrigger(false)}>close</button>
                {props.children}
                <Connect></Connect>
            </div>
        </div>
    ) : "";
}

export default Popup;
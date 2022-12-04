import React, {useState} from "react";
import {over} from 'stompjs';
import SockJS from 'sockjs-client';
import Popup from "./Popup";

export default function Connect() {
    const [username] = useState((JSON.parse(localStorage.getItem("user"))).username);
    const [message, setMessage] = useState("");
    const [justStarted, setJustStarted] = useState(true);
    const [popupTriggered, setPopupTriggered] = useState(false);
    const [messageArray, setMessageArray] = useState({});

    let Sock = new SockJS("http://localhost:8080/ws");
    let stompClient = over(Sock);

    const sendSignalToServer = () => {
        if(stompClient){
            stompClient.send('/app/private-message', {}, username);  // i send the username of the logged-in user as a signal
            setMessage("");
            setJustStarted(false);
        }
    }

    const onPrivateMessageReceived = (payload) => {
        setMessage((payload.body).split("***"));
        if(payload.body===username) {
            setPopupTriggered(false);
        }else{
            setPopupTriggered(true);
        }
    }

    const onConnected = () => {
        if(justStarted){
            sendSignalToServer();
        }
        stompClient.subscribe('/user/' + username + '/private', onPrivateMessageReceived)
    }

    const onError = () => {
        console.log("error");
    }

    stompClient.connect({}, onConnected, onError);

    return (
        <Popup trigger={popupTriggered} setTrigger={setPopupTriggered}>
            <h5>{message}</h5>
        </Popup>
    )
}


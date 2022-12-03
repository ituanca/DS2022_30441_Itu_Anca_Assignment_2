import React, {useState} from "react";
import {over} from 'stompjs';
import SockJS from 'sockjs-client';

export default function Connect() {
    const [user, setUser] = useState((JSON.parse(localStorage.getItem("user"))).username);
    const [message, setMessage] = useState([]);

    const onConnected = () => {
        stompClient.subscribe('/user/' + user + '/private', onPrivateMessageReceived)
    }

    const onPrivateMessageReceived = (payload) => {
        console.log("payload: " + payload.body.toString())
        let payloadData = JSON.parse(payload);
        setMessage(payloadData);
    }

    const onError = () => {
        console.log("error");
    }

    let Sock = new SockJS("http://localhost:8080/ws");
    let stompClient = over(Sock);
    stompClient.connect({}, onConnected, onError);

    console.log("message: " + message);

    return (
        <div>
            <h5>{message}</h5>
        </div>
    )
}


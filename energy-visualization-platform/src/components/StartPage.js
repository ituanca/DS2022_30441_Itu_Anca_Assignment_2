import React from "react";
import {Link, Outlet} from "react-router-dom";
import './StartPage.css';

function StartPage(){
    return (
        <div>
            <label className="title"><strong>Online Energy Utility Platform</strong></label>
            <nav>
                <div>
                    <Link to="/LogIn">
                        <button className="log-in-button">Log in</button>
                    </Link>
                </div>
            </nav>
            <Outlet />
        </div>
    );
}

export default StartPage;
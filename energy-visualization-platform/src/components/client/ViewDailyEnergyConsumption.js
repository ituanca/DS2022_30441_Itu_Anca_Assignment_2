import React, {useEffect, useState} from "react";
import {Link, Outlet} from "react-router-dom";
import "./ViewDailyEnergyConsumption.css"
import axios from "axios";
import {Chart as ChartJS, CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend} from "chart.js";
import {Bar} from "react-chartjs-2";

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend);

function ViewDailyEnergyConsumption(){

    const [dateIsSelected, setDateIsSelected] = useState(false);
    const [buttonEnabled, setButtonEnabled] = useState(false);
    const [chartsVisible, setChartsVisible] = useState(false);
    const [device, setDevice] = useState({
        name: "",
        description: "",
        address: "",
        maxHourlyEnergyConsumption: "",
        hourlyConsumption: {}
    });
    const [energyConsumptionList, setEnergyConsumptionList] = useState( [] );
    const [selectedDate, setSelectedDate] = useState("");
    const [user, setUser] = useState(JSON.parse(localStorage.getItem("user")));
    const [devices, setDevices] = useState( [] );

    const [chartData, setChartData] = useState({
        datasets: []
    });
    const [chartOptions, setChartOptions] = useState({});

    const hours = ["00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00",
        "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00",
        "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"];

    useEffect(() => {
        setUser(JSON.parse(localStorage.getItem("user")));
        axios
            .get('http://localhost:8080/person/ownedDevicesWithHourlyConsumption', {
                params: {
                    username: user.username
                }
            })
            .then((response) => {
                setDevices(response.data);
                setDevice(response.data[0])
            })
            .catch((error) => {
                console.log(error);
            });
    }, []);

    useEffect( () => {
        if(dateIsSelected){
            console.log("inside")
            axios
                .get('http://localhost:8080/hourlyEnergyConsumption/energyConsumption', {
                    params: {
                        deviceName: device.name,
                        date: selectedDate
                    }
                })
                .then((response) => {
                    console.log(response.data)
                    setEnergyConsumptionList(response.data);
                })
                .catch((error) => {
                    console.log(error);
                });
        }
    }, [selectedDate, device])

    const handleSubmit = (event) => {
        event.preventDefault();

        if(dateIsSelected){
            setChartsVisible(true);
            setChartData({
                labels: hours,
                datasets: [
                    {
                        label: "energy consumption(kWh)",
                        data: energyConsumptionList,
                        borderColor: "rgb(53, 162, 235)",
                        backgroundColor: "rgba(53, 162, 235, 0.7)",
                    }
                ]
            });
            setChartOptions({
                responsive: true,
                plugins: {
                    legend: {
                        position: "top"
                    },
                    title: {
                        display: true,
                        text: "Energy consumption on " + selectedDate + " for the device " + device.name,
                    }
                }
            })
        }

    };

    const renderFormWithComboBox = (
        <form>
            <div className="input-container-col">
                <label>Select a device:</label>
                <select className="select-device-mini-form" onChange={event => {
                    for(let i = 0; i < devices.length; i++){
                        if (event.target.value === devices.at(i).name){
                            setDevice(devices.at(i))
                        }
                    }
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

    const renderFormWithDateSelector = (
        <form>
            <div className="input-container-col">
                <label>Select a date:</label>
                <input type="date"
                       value={selectedDate.toString()}
                       onChange={event => {
                           setSelectedDate(event.target.value);
                           setDateIsSelected(true);
                       }}
                       name="date" required/>
            </div>
        </form>
    );

    useEffect(() => {
        if(dateIsSelected){
            setButtonEnabled(true);
        }else{
            setButtonEnabled(false);
        }
    }, [])

    const renderFormWithDropdownAndDateSelector = (
        <form onSubmit={handleSubmit}>
            <div className="container-multiple-cols">
                <div className="form-container-dropdown">
                    {renderFormWithComboBox}
                </div>
                <div className="form-container-date-selector">
                    {renderFormWithDateSelector}
                </div>
                <div className="form-container-button-view-chart">
                    <button type="submit" disabled={buttonEnabled}>View chart</button>
                </div>
            </div>
        </form>
    )

    const renderFormWithChart = (
        <div>
           <Bar options={chartOptions} data={chartData} />
            <span>&nbsp;&nbsp;</span>
        </div>
    );

    const render = () => {
        if(devices!==null && devices.length > 0){
            return (
                <div>
                    {renderFormWithDropdownAndDateSelector}
                    {chartsVisible ? renderFormWithChart : null}
                </div>
            );
        }else{
            return (
                <h4 className="h4-no-devices-view-energy-consumption">You do not own any devices</h4>
            );
        }
    };

    return (
        <div className="app">
            <div className="login-form">
                <h3>View the daily energy consumption of your devices</h3>
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

export default ViewDailyEnergyConsumption;
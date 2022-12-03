import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import {BrowserRouter, Route, Routes} from "react-router-dom";
import LogIn from "./components/LogIn";
import AdminActions from "./components/admin/AdminActions";
import CreateUser from "./components/admin/userActions/CreateUser";
import ReadUser from "./components/admin/userActions/ReadUser";
import UpdateUser from "./components/admin/userActions/UpdateUser";
import ClientActions from "./components/client/ClientActions";
import ViewDevices from "./components/client/ViewDevices";
import CreateDevice from "./components/admin/deviceActions/CreateDevice";
import ViewDevice from "./components/admin/deviceActions/ViewDevice";
import UpdateDevice from "./components/admin/deviceActions/UpdateDevice";
import DeleteUser from "./components/admin/userActions/DeleteUser";
import DeleteDevice from "./components/admin/deviceActions/DeleteDevice";
import CreateMappings from "./components/admin/mappingActions/CreateMappings";
import DeleteMappings from "./components/admin/mappingActions/DeleteMappings";
import EditMappings from "./components/admin/mappingActions/EditMappings";
import ViewMappings from "./components/admin/mappingActions/ViewMappings";
import ViewDailyEnergyConsumption from "./components/client/ViewDailyEnergyConsumption";
import Connect from "./components/webSocketConnection/Connection";
import Popup from "./components/webSocketConnection/Popup";

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
      <BrowserRouter>
          <Routes>
              <Route path="/" element={<App />} />
              <Route path="/LogIn" element={<LogIn />} />
              <Route path="/AdminActions" element={<AdminActions />} />
              <Route path="/CreateUser" element={<CreateUser />} />
              <Route path="/ViewUsers" element={<ReadUser />} />
              <Route path="/EditUsers" element={<UpdateUser />} />
              <Route path="/DeleteUsers" element={<DeleteUser />} />
              <Route path="/CreateDevice" element={<CreateDevice />} />
              <Route path="/ViewDevice" element={<ViewDevice />} />
              <Route path="/EditDevices" element={<UpdateDevice />} />
              <Route path="/DeleteDevices" element={<DeleteDevice />} />
              <Route path="/CreateMappings" element={<CreateMappings />} />
              <Route path="/ViewMappings" element={<ViewMappings />} />
              <Route path="/EditMappings" element={<EditMappings />} />
              <Route path="/DeleteMappings" element={<DeleteMappings />} />
              <Route path="/ClientActions" element={<ClientActions />} />
              <Route path="/ViewDevices" element={<ViewDevices />} />
              <Route path="/ViewDailyEnergyConsumption" element={<ViewDailyEnergyConsumption />} />
              <Route path="/Connect" element={<Connect />} />
              <Route path="/Popup" element={<Popup />} />
          </Routes>
      </BrowserRouter>,
  </React.StrictMode>
);

reportWebVitals();

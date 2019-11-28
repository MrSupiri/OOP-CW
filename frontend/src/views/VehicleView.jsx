import React, {useState, Fragment} from 'react';
import VehicleCard from "../components/VehicleCard";
import "../App.css"
import TextField from '@material-ui/core/TextField';
import {textInputHandleChanger} from "../libs/InputHandler";

const VehicleView = (props) => {
    const [vehicleModel, setVehicleModel] = useState("");
    const [plateNumber, setPlateNumber] = useState("");
    const [transmission, setTransmission] = useState("");

    return(
        <Fragment>
            <TextField label="Vehicle Type" name="vehicleType" className={"filter"}
                       onChange={(e) => textInputHandleChanger(e, setVehicleModel)} type="text" />
            <TextField label="Plate Number" name="plateNumber" className={"filter"}
                       onChange={(e) => textInputHandleChanger(e, setPlateNumber)} type="text" />
            <TextField label="Transmission" name="transmission" className={"filter"}
                       onChange={(e) => textInputHandleChanger(e, setTransmission)} type="text" />
            {props.vehicles.map((vehicle) => {
                if (vehicle.vehicleModel.type.toLowerCase().includes(vehicleModel.toLowerCase())
                    && vehicle.plateNumber.toLowerCase().includes(plateNumber.toLowerCase())
                    && vehicle.transmission.toLowerCase().includes(transmission.toLowerCase())) {
                    return (
                        <VehicleCard key={vehicle.plateNumber} vehicle={vehicle} days={props.days} rentAVehicle={props.rentAVehicle}/>
                    )
                }
                return null
            })}
        </Fragment>
    )
};

export default VehicleView
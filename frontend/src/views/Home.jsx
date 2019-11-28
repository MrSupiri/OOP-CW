import React, {Component} from 'react';
import axios from 'axios';
import TextField from '@material-ui/core/TextField';
import BookForm from "../components/BookForm"
import VehicleCard from "../components/VehicleCard";
import "../App.css"
import Grid from '@material-ui/core/Grid';
import {textInputStateHandleChanger} from "../libs/InputHandler";

class VehicleList extends Component {

    constructor(props) {
        super(props);
        this.state = {
            data: [],
            selectedVehicle: null,
            vehicleType: "",
            plateNumber: "",
            transmission: "",
        };
    }

    componentDidMount() {
        axios.post('http://localhost:4567/api/user/vehicle/', {
            pickUpDate: new Date(),
            dropOffDate: new Date()
        }, {headers: {'Content-Type': 'application/json'}})
            .then((res) => this.setState({data: res.data}))
    }

    render() {
        return (
            <div style={{marginTop: "100px"}}>
                <Grid container id={"GridContainer"}>
                    <TextField label="Vehicle Type" name="vehicleType" className={"filter"}
                               onChange={(e) => textInputStateHandleChanger(e, this)} type="text" />
                    <TextField label="Plate Number" name="plateNumber" className={"filter"}
                               onChange={(e) => textInputStateHandleChanger(e, this)} type="text" />
                    <TextField label="Transmission" name="transmission" className={"filter"}
                               onChange={(e) => textInputStateHandleChanger(e, this)} type="text" />
                    {this.state.selectedVehicle && <BookForm selectedVehicle={this.state.selectedVehicle}/>}
                    {this.state.data.map((vehicle) => {
                        if (vehicle.vehicleModel.type.toLowerCase().includes(this.state.vehicleType.toLowerCase())
                            && vehicle.plateNumber.toLowerCase().includes(this.state.plateNumber.toLowerCase())
                            && vehicle.transmission.toLowerCase().includes(this.state.transmission.toLowerCase())) {
                            return (
                                <VehicleCard {...vehicle} days={1}/>
                            )
                        }
                        return null
                    })}
                </Grid>
            </div>
        );
    }
}

export default VehicleList;
import React, {Component} from 'react';
import Axios from 'axios';
import RentPeriod from "./RentPeriod";
import "../App.css"
import Grid from '@material-ui/core/Grid';
import VehicleView from "./VehicleView";
import ReservationForm from "../components/ReservationForm"
import Button from '@material-ui/core/Button';
import {addDays} from '../libs/DateUtils'


class Home extends Component {

    constructor(props) {
        super(props);
        const today = new Date();
        today.setHours(0,0,0,0);
        this.state = {
            vehicles: [],
            period: 0,
            selectedVehicle: null,
            showDialog: false,
            pickUpDate: today,
            dropOffDate: addDays(today, 1)
        };
    }

    rentAVehicle = (vehicle) => {
        this.setState({selectedVehicle: vehicle, showDialog: true});
    };

    toggleDialog = () => {
        this.setState(prevState => ({
            showDialog: !prevState.showDialog
        }));
    };

    fetchVehicles = (pickUpDate, dropOffDate) => {
        Axios.post('http://localhost:4567/api/user/vehicle/', {
            pickUpDate,
            dropOffDate
        }, {headers: {'Content-Type': 'application/json'}})
            .then((res) => this.setState({
                vehicles: res.data,
                period: Math.round(Math.round(dropOffDate.getTime() - pickUpDate.getTime()) / (1000 * 60 * 60 * 24)),
                pickUpDate: pickUpDate,
                dropOffDate: dropOffDate
            }))
    };

    render() {
        return (
            <div style={{marginTop: "100px"}}>
                {this.state.vehicles.length === 0 ?
                    <RentPeriod fetchVehicles={this.fetchVehicles} pickUpDate={this.state.pickUpDate} dropOffDate={this.state.dropOffDate} />
                    :
                    <Grid container id={"GridContainer"}>
                        <VehicleView vehicles={this.state.vehicles} days={this.state.period}
                                     rentAVehicle={this.rentAVehicle}/>
                        <Button color="secondary" onClick={() => this.setState({ vehicles: []})}>Back</Button>

                    </Grid>
                }
                {this.state.selectedVehicle &&
                <ReservationForm
                    vehicle={this.state.selectedVehicle}
                    showDialog={this.state.showDialog}
                    toggleDialog={this.toggleDialog}
                    pickupDate={this.state.pickUpDate}
                    dropOffDate={this.state.dropOffDate}
                />
                }
            </div>
        );
    }
}

export default Home;
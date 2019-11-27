import React, { Component } from 'react';
import axios from 'axios';
import TextField from '@material-ui/core/TextField';
import BookForm from "../components/BookForm"
import VehicleCard from "../components/VehicleCard";
import "../App.css"

class VehicleList extends Component {

    constructor(props) {
        super(props);
        this.state = {
            data: [],
            selectedVehicle: null,
        };
    }

    componentDidMount() {
        axios.post('http://localhost:4567/api/user/vehicle/', { pickUpDate: new Date(), dropOffDate: new Date() }, { headers: { 'Content-Type': 'application/json' } })
            .then((res) => this.setState({ data: res.data }))
    }

    render() {
        return (
            <div style={{marginTop: "100px"}}>
                <TextField
                    label="Password"
                    type="text"
                    margin="normal"
                />
                {this.state.selectedVehicle && <BookForm selectedVehicle={this.state.selectedVehicle} />}
                {this.state.data.map((vehicle) => {
                    return(
                        <VehicleCard {...vehicle} days={1}/>
                    )
                })}
            </div>
        );
    }
}

export default VehicleList;
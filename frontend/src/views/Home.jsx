import React, { Component } from 'react';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Paper from '@material-ui/core/Paper';
import Button from '@material-ui/core/Button';
import axios from 'axios';
import BookForm from "../components/BookForm"


class VehicleList extends Component {

    constructor() {
        super();
        this.state = {
            data: [],
            selectedVehicle: null,
        }


    }
    componentDidMount() {
        axios.post('http://localhost:4567/api/user/vehicle/', { startDate: new Date(), endDate: new Date() }, { headers: { 'Content-Type': 'application/json' } })
            .then((res) => this.setState({ data: res.data }))
    }

    render() {

        console.log(this.state.data)
        return (
            <div>
                <Paper >
                    <Table aria-label="simple table">
                        <TableHead>
                            <TableRow>
                                <TableCell>PlateNumber</TableCell>
                                <TableCell>CostPerDay</TableCell>
                                <TableCell>Vehicle Make</TableCell>
                                <TableCell>Vehicle Type</TableCell>
                                <TableCell>Vehicle Model</TableCell>
                                <TableCell>Mileage</TableCell>
                                <TableCell>EngineCapacity</TableCell>
                                <TableCell>Seats</TableCell>
                                <TableCell>Transmission</TableCell>
                                <TableCell>Book</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {this.state.data.map((row) => {
                                console.log(this.state.data)
                                return (
                                    <TableRow key={row.plateNumber}>
                                        <TableCell component="th" scope="row">
                                            {row.plateNumber}
                                        </TableCell>
                                        <TableCell>{row.costPerDay}</TableCell>
                                        <TableCell>{row.vehicleModel.make}</TableCell>
                                        <TableCell>{row.vehicleModel.type}</TableCell>
                                        <TableCell>{row.vehicleModel.model}</TableCell>
                                        <TableCell>{row.mileage}</TableCell>
                                        <TableCell>{row.engineCapacity}</TableCell>
                                        <TableCell>{row.seats}</TableCell>
                                        <TableCell>{row.transmission}</TableCell>
                                        <TableCell><Button onClick={() => this.setState({ selectedVehicle: row.plateNumber })} variant="contained" color="secondary">Rent</Button></TableCell>
                                    </TableRow>
                                )
                            })}
                        </TableBody>
                    </Table>
                </Paper>
                {this.state.selectedVehicle && <BookForm selectedVehicle={this.state.selectedVehicle} />}
            </div>
        );
    }
}

export default VehicleList;
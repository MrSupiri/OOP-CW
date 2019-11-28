import React, {useState} from 'react';
import Button from '@material-ui/core/Button';
import TextField from '@material-ui/core/TextField';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';
import {textInputHandleChanger} from "../libs/InputHandler";
import { makeStyles } from '@material-ui/core/styles';
import { useSnackbar } from 'notistack';
import axios from "axios"

const useStyles = makeStyles({
    textBox: {
        margin: "10px 0 10px 0",
    },
});

export default function ReservationForm(props) {
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [phoneNumber, setPhoneNumber] = useState("");
    const classes = useStyles();
    const { enqueueSnackbar } = useSnackbar();

    const makeReservation = (e) => {
        e.preventDefault();
        axios.post('http://localhost:4567/api/user/book/', {
            firstName: firstName,
            lastName: lastName,
            phoneNumber: phoneNumber,
            pickupDate: props.pickupDate,
            dropOffDate: props.dropOffDate,
            plateNumber: props.vehicle.plateNumber,
        }, { headers: { 'Content-Type': 'application/json' } })
            .then((res) => {
                res.data ? enqueueSnackbar(res.data.success, {variant: "success"}) : enqueueSnackbar("Backend is offline", {variant: "error"})
            })
            .catch((err) => {
                err.response.data ? enqueueSnackbar(err.response.data.error, {variant: "error"}) : enqueueSnackbar("Backend is offline", {variant: "error"})
            });
        props.toggleDialog();
    };

    return (
        <Dialog open={props.showDialog} onClose={props.toggleDialog} aria-labelledby="form-dialog-title">
            <form onSubmit={makeReservation}>
            <DialogTitle id="form-dialog-title">Reserve {props.vehicle.plateNumber}</DialogTitle>
            <DialogContent>
                <DialogContentText>
                    You are about to
                    reserve {props.vehicle.vehicleModel.make} {props.vehicle.vehicleModel.model} from {props.pickupDate.toDateString()} to {props.dropOffDate.toDateString()} EUR {props.vehicle.rent}
                </DialogContentText>
                <TextField
                    name="firstName"
                    variant="outlined"
                    required
                    fullWidth
                    id="firstName"
                    label="First Name"
                    autoFocus
                    className={classes.textBox}
                    onChange={(e) => {
                        textInputHandleChanger(e, setFirstName)
                    }}
                />
                <TextField
                    variant="outlined"
                    required
                    fullWidth
                    id="lastName"
                    label="Last Name"
                    name="lastName"
                    className={classes.textBox}
                    onChange={(e) => {
                        textInputHandleChanger(e, setLastName)
                    }}
                />
                <TextField
                    variant="outlined"
                    required
                    fullWidth
                    id="phoneNumber"
                    label="Phone Number"
                    name="phoneNumber"
                    className={classes.textBox}
                    onChange={(e) => {
                        textInputHandleChanger(e, setPhoneNumber)
                    }}
                    inputProps={{pattern: "^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$"}}
                />
            </DialogContent>
            <DialogActions>
                <Button onClick={props.toggleDialog} color="primary">
                    Cancel
                </Button>
                <Button type={"submit"} color="primary">
                    Reserve
                </Button>
            </DialogActions>
            </form>
        </Dialog>
    );
}
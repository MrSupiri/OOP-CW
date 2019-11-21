import React, { useState } from 'react';
import Button from '@material-ui/core/Button';
import CssBaseline from '@material-ui/core/CssBaseline';
import TextField from '@material-ui/core/TextField';
import Grid from '@material-ui/core/Grid';
import Typography from '@material-ui/core/Typography';
import { makeStyles } from '@material-ui/core/styles';
import Container from '@material-ui/core/Container';
import { DateTimePicker } from "@material-ui/pickers";
import axios from 'axios';
import {textInputHandleChanger, dateInputHandleChanger} from "../libs/InputHandler"
import { useSnackbar } from 'notistack';

const useStyles = makeStyles(theme => ({
    '@global': {
        body: {
            backgroundColor: theme.palette.common.white,
        },
    },
    paper: {
        marginTop: theme.spacing(8),
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        width: "500px"
    },
    avatar: {
        margin: theme.spacing(1),
        backgroundColor: theme.palette.secondary.main,
    },
    form: {
        width: '100%', // Fix IE 11 issue.
        marginTop: theme.spacing(3),
    },
    submit: {
        margin: theme.spacing(3, 0, 2),
    },
    time: {
        width: "230px"
    }
}));


export default function BookForm(props) {
    const classes = useStyles();
    const [firstName, setfirstName] = useState("");
    const [lastName, setlastName] = useState("");
    const [email, setEmail] = useState("");
    const [pickupDate, setPickUpDate] = useState(new Date());
    const [dropOffDate, setDropOffDate] = useState(new Date());
    const { enqueueSnackbar } = useSnackbar();

    const makeReservation = (e) => {
        e.preventDefault();
        axios.post('http://localhost:4567/api/user/book/', {
            firstName: firstName,
            lastName: lastName,
            email: email,
            pickupDate: pickupDate,
            dropOffDate: dropOffDate,
            plateNumber: props.selectedVehicle,
        }, { headers: { 'Content-Type': 'application/json' } })
            .then((res) => enqueueSnackbar(res.data.success, {variant: "success"}))
            .catch((res) => enqueueSnackbar(res.data.error, {variant: "error"}))
    }

    return (
        <Container component="main" maxWidth="xs">
            <CssBaseline />
            <div className={classes.paper}>
                <Typography component="h1" variant="h5">
                    Make Reservation for {props.selectedVehicle}
                </Typography>
                <form className={classes.form} onSubmit={makeReservation}>
                    <Grid container spacing={2}>
                        <Grid item xs={12} sm={6}>
                            <TextField
                                autoComplete="fname"
                                name="firstName"
                                variant="outlined"
                                required
                                fullWidth
                                id="firstName"
                                label="First Name"
                                autoFocus
                                onChange={(e) => { textInputHandleChanger(e, setfirstName) }}
                            />
                        </Grid>
                        <Grid item xs={12} sm={6}>
                            <TextField
                                variant="outlined"
                                required
                                fullWidth
                                id="lastName"
                                label="Last Name"
                                name="lastName"
                                autoComplete="lname"
                                onChange={(e) => { textInputHandleChanger(e, setlastName) }}
                            />
                        </Grid>
                        <Grid item xs={12}>
                            <TextField
                                variant="outlined"
                                required
                                fullWidth
                                id="email"
                                label="Email Address"
                                name="email"
                                autoComplete="email"
                                onChange={(e) => { textInputHandleChanger(e, setEmail) }}
                            />
                        </Grid>
                        <Grid item xs={12} sm={6}>
                            <DateTimePicker
                                variant="inline"
                                className={classes.time}
                                label="PickUp Date"
                                value={pickupDate}
                                onChange={(date) => {dateInputHandleChanger(date, setPickUpDate)}}
                            />
                        </Grid>
                        <Grid item xs={12} sm={6}>
                            <DateTimePicker
                                className={classes.time}
                                variant="inline"
                                label="DropOff Date"
                                value={dropOffDate}
                                onChange={(date) => {dateInputHandleChanger(date, setDropOffDate)}}
                            />
                        </Grid>
                    </Grid>


                    <Button
                        type="submit"
                        fullWidth
                        variant="contained"
                        color="primary"
                        className={classes.submit}
                    >
                        Reserve
                    </Button>
                </form>
            </div>
        </Container >
    );
}
import React, {useState} from 'react';
import Button from '@material-ui/core/Button';
import Typography from '@material-ui/core/Typography';
import {makeStyles} from '@material-ui/core/styles';
import Container from '@material-ui/core/Container';
import {DatePicker} from "@material-ui/pickers";
import {dateInputHandleChanger} from "../libs/InputHandler"
import {addDays} from '../libs/DateUtils'

const useStyles = makeStyles(theme => ({
    form: {
        width: '230px',
    },
    submit: {
        margin: theme.spacing(3, 0, 2),
    },
    time: {
        width: "230px",
        marginTop: "40px",
    },
    container: {}
}));


export default function BookForm(props) {

    const classes = useStyles();
    const [pickupDate, setPickUpDate] = useState(props.pickUpDate);
    const [dropOffDate, setDropOffDate] = useState(props.dropOffDate);

    return (
        <Container component="main" maxWidth="xs">
            <Typography component="h1" variant="h5">
                Select the Time Period
            </Typography>
            <form className={classes.form} onSubmit={(e) => {
                e.preventDefault();
                props.fetchVehicles(pickupDate, dropOffDate)
            }}>
                <DatePicker
                    className={classes.time}
                    label="PickUp Date"
                    minDate={new Date()}
                    value={pickupDate}
                    format="dd MMMM yyyy"
                    onChange={(date) => {
                        dateInputHandleChanger(date, setPickUpDate)
                    }}
                />

                <DatePicker
                    className={classes.time}
                    label="DropOff Date"
                    value={dropOffDate}
                    minDate={addDays(pickupDate, 1)}
                    format="dd MMMM yyyy"
                    onChange={(date) => {
                        dateInputHandleChanger(date, setDropOffDate)
                    }}
                />

                <Button
                    type="submit"
                    fullWidth
                    variant="contained"
                    color="primary"
                    className={classes.submit}
                >
                    Get Available Vehicles
                </Button>
            </form>
        </Container>
    );
}
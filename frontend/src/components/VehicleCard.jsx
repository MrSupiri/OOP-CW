import React, {Fragment} from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Grid from '@material-ui/core/Grid';
import Button from '@material-ui/core/Button';

const useStyles = makeStyles({
    root: {
        flexGrow: 1,
        margin: "10px 0 10px 0",
        padding: "0 20px 0 20px",
        justifyContent: "center",
    },
    debug: {
        border: "1px solid black;",
    },
    vehicleModel: {
        marginTop: "0px",
    },
    rentButton: {
        background: "#00c853",
        color: "#EEE",
        width: "90%"
    },
    rentWrapper: {
        textAlign: "right",
    }
});



export default function VehicleCard(props) {
    const classes = useStyles();

    // https://stackoverflow.com/questions/196972/convert-string-to-title-case-with-javascript
    String.prototype.toProperCase = function () {
        return this.replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();});
    };
    return (
        <Grid container className={classes.root}>
            <Grid item xs={2}>
                <img src={`https://source.unsplash.com/215x150/?${props.vehicleModel.type}`} alt={props.vehicleModel.type} />
            </Grid>
            <Grid item xs={4}>
                <span>{props.vehicleModel.type}</span>
                <h2 className={classes.vehicleModel} >{props.vehicleModel.make} {props.vehicleModel.model}</h2>
                <Grid className={classes.featureWrapper} container>
                    <Grid item xs={6}>
                        {props.mileage} Mileage
                    </Grid>
                    <Grid item xs={6}>
                        {props.engineCapacity}CC Engine Capacity
                    </Grid>
                    <Grid item xs={6}>
                        {props.seats} Seats
                    </Grid>
                    <Grid item xs={6}>
                        {props.transmission.toProperCase()} Transmission
                    </Grid>
                    {props.doors ?
                        <Fragment>
                            <Grid item xs={6}>
                                {props.doors} Doors
                            </Grid>
                            < Grid item xs={6}>
                                {props.airConditioned ? "Air Conditioned": "Non Air Conditioned"}
                            </Grid>
                            <Grid item xs={6}>
                                {props.trunkCapacity} Bags
                            </Grid>
                        </Fragment>
                        :
                        <Fragment>
                            <Grid item xs={6}>
                                {props.wheelSize}" Wheel Size
                            </Grid>
                            < Grid item xs={6}>
                                {props.sideCar ? "Has an Side Car": "No Side Car"}
                            </Grid>
                            <Grid item xs={6}>
                                {props.numOfHelmets} Helmets
                            </Grid>
                        </Fragment>
                    }

                </Grid>
            </Grid>
            <Grid item className={classes.rentWrapper} xs={2}>
                <span>Price for {props.days} day</span>
                <h2>EUR {(props.costPerDay * props.days).toFixed(2) }</h2>
                <Button className={classes.rentButton} onClick={() => null} color="secondary" variant="contained">Rent</Button>
            </Grid>
        </Grid>
    );
}
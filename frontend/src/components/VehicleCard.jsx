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
    sectionWrapper: {
        background: "#fff",
        padding: "10px 0 10px 15px",
        // border: "1px solid black;",
    },
    vehicleModel: {
        margin: "0px",
    },
    rentButton: {
        background: "#00c853",
        color: "#EEE",
        width: "90%"
    },
    rentWrapper: {
        textAlign: "right",
    },
    featureWrapper: {
        marginTop: "10px",
    }
});



export default function VehicleCard(props) {
    const classes = useStyles();

    // https://stackoverflow.com/questions/196972/convert-string-to-title-case-with-javascript
    // eslint-disable-next-line
    String.prototype.toProperCase = function () {
        return this.replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();});
    };
    props.vehicle.rent = (props.vehicle.costPerDay * props.days).toFixed(2);
    return (
        <Grid container className={classes.root}>
            <Grid item className={classes.sectionWrapper} xs={2}>
                <img src={`https://source.unsplash.com/215x150/?${props.vehicle.vehicleModel.type}`} alt={props.vehicle.vehicleModel.type} />
            </Grid>
            <Grid item className={classes.sectionWrapper} xs={4}>
                <span>{props.vehicle.vehicleModel.type}</span>
                <h2 className={classes.vehicleModel} >{props.vehicle.vehicleModel.make} {props.vehicle.vehicleModel.model}</h2>
                <span>{props.vehicle.plateNumber}</span>
                <Grid className={classes.featureWrapper} container>
                    <Grid item xs={6}>
                        {props.vehicle.mileage} Mileage
                    </Grid>
                    <Grid item xs={6}>
                        {props.vehicle.engineCapacity}CC Engine Capacity
                    </Grid>
                    <Grid item xs={6}>
                        {props.vehicle.seats} Seats
                    </Grid>
                    <Grid item xs={6}>
                        {props.vehicle.transmission.toProperCase()} Transmission
                    </Grid>
                    {props.vehicle.doors ?
                        <Fragment>
                            <Grid item xs={6}>
                                {props.vehicle.doors} Doors
                            </Grid>
                            < Grid item xs={6}>
                                {props.vehicle.airConditioned ? "Air Conditioned": "Non Air Conditioned"}
                            </Grid>
                            <Grid item xs={6}>
                                {props.vehicle.trunkCapacity} Bags
                            </Grid>
                        </Fragment>
                        :
                        <Fragment>
                            <Grid item xs={6}>
                                {props.vehicle.wheelSize}" Wheel Size
                            </Grid>
                            < Grid item xs={6}>
                                {props.vehicle.sideCar ? "Has an Side Car": "No Side Car"}
                            </Grid>
                            <Grid item xs={6}>
                                {props.vehicle.numOfHelmets} Helmets
                            </Grid>
                        </Fragment>
                    }

                </Grid>
            </Grid>
            <Grid item className={classes.sectionWrapper} xs={2}>
                <span>Price for {props.days} day</span>
                <h2>EUR  {props.vehicle.rent }</h2>
                <Button className={classes.rentButton} onClick={() => props.rentAVehicle(props.vehicle)} color="secondary" variant="contained">Rent Now</Button>
            </Grid>
        </Grid>
    );
}
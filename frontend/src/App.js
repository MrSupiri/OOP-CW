import React from 'react';
import Home from "./views/Home"
import { MuiPickersUtilsProvider } from '@material-ui/pickers';
import DateFnsUtils from '@date-io/date-fns';
import { SnackbarProvider } from 'notistack';


function App() {
    return (
        <div className="App">
            <SnackbarProvider maxSnack={4}>
                <MuiPickersUtilsProvider utils={DateFnsUtils}>
                    <Home />
                </MuiPickersUtilsProvider>
            </SnackbarProvider>
        </div>
    );
}

export default App;

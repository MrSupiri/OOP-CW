import React from 'react';
import Home from "./views/Home"
import { MuiPickersUtilsProvider } from '@material-ui/pickers';
import DateFnsUtils from '@date-io/date-fns';

function App() {
    return (
        <div className="App">
            <MuiPickersUtilsProvider utils={DateFnsUtils}>
                <Home />
            </MuiPickersUtilsProvider>
        </div>
    );
}

export default App;

export const textInputHandleChanger = (event, func) => {
    func(event.target.value);
};

export const textInputStateHandleChanger = (event, obj) => {
    obj.setState({[event.target.name]: event.target.value});
};

export const dateInputHandleChanger = (date, func) => {
    func(date);
};


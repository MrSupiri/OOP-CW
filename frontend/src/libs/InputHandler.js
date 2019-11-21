export const textInputHandleChanger = (event, func) => {
    func(event.target.value);
}

export const dateInputHandleChanger = (date, func) => {
    func(date);
}


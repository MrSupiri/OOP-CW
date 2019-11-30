export const addDays = (currentDate, days) => {
    const date = new Date(currentDate.toISOString());
    date.setDate(date.getDate() + days);
    return date;
};
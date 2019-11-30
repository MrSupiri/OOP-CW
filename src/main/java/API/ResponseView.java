package API;

/***
 * Json ResponseView to send data to Front Ends,
 * If the action user did was successful success attribute will contains the generated data from that action
 * If it resulted in an error that error will be stored in the error attribute
 */
public class ResponseView {
    private Object success = null;
    private Object error = null;

    ResponseView(Object success, Object error) {
        this.success = success;
        this.error = error;
    }

    public Object getSuccess() {
        return success;
    }

    public Object getError() {
        return error;
    }

}

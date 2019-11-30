package API;

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

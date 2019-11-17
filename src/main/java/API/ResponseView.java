package API;

public class ResponseView {
    private Object success = null;
    private Object error = null;
    private Integer value = null;

    public ResponseView(Object success, Object error) {
        this.success = success;
        this.error = error;
    }

    public Object getSuccess() {
        return success;
    }

    public Object getError() {
        return error;
    }

    public Integer getValue() {
        return value;
    }
}

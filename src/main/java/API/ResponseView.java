package API;

public class ResponseView {
    private String success = "";
    private String error = "";

    public ResponseView(String success, String error) {
        this.success = success;
        this.error = error;
    }

    public String getSuccess() {
        return success;
    }

    public String getError() {
        return error;
    }
}

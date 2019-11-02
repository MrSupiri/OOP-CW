package Models;

public class VehicleModel {
    private String type;
    private String make;
    private String model;

    public VehicleModel(String type, String make, String model) {
        this.type = type;
        this.make = make;
        this.model = model;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
